(ns clj-noobie.datomic.logic
  (:use [clojure.pprint])
  (:require
    [datomic.api :as d]
    [clj-noobie.datomic.product :as d.product]
    [clj-noobie.datomic.db :as d.db]))

(def conn
  (d.db/create-connection))

;(d.db/delete-database)
(d.db/create_schema conn)

(let [computer (d.product/build_product "Notebook" "notebook" 7888.88M)]
  (d/transact conn [computer]))

(def db (d/db conn))

;; Looking for registered entities that contains :product/name
(d/q '[:find ?entity
       :where [?entity :product/name]] db)
