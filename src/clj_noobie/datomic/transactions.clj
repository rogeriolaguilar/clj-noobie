(ns clj-noobie.datomic.transactions
  (:use [clojure.pprint])
  (:require
    [datomic.api :as d]
    [clj-noobie.datomic.product :as product]
    [clj-noobie.datomic.db :as db]))



(defn add-product
  "Add product with metadata in its transaction"
  [conn product ip]
  (let [tx-add-ip [:db/add "datomic.tx" :tx-data/ip ip]]    ;datomic.tx its the current transaction
    ;(pprint (conj product tx-add-ip))
    (d/transact conn (conj product tx-add-ip))))


(let [conn (db/reset-db!)
      product (product/build-product "P1" "p1" 100M)
      ip "192.168.0.1"]

  (add-product conn [product] ip)

  (d/q '[:find (pull ?product [*])
         :in $ ?ip
         :where [?transaction :tx-data/ip ?ip]
                [?product :product/id _ ?transaction]]
         (d/db conn) ip)
  )