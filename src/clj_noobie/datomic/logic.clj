(ns clj-noobie.datomic.logic
  (:use [clojure.pprint])
  (:require
    [datomic.api :as d]
    [clj-noobie.datomic.product :as d.product]
    [clj-noobie.datomic.db :as d.db]))

(def conn
  (d.db/create-connection!))

;(d.db/delete-database)
(d.db/create_schema! conn)

(let [computer (d.product/build-product "Notebook" "notebook" 7888.88M)]
  (d/transact conn [computer]))

;; Looking for registered entities that contains :product/name
(d/q '[:find ?entity
       :where [?entity :product/name]] (d/db conn))

; including just one off the identities :product/name
(let [calculator {:product/name "Calculator"}]
  (d/transact conn [calculator]))


;; Returns error
(let [watch {:product/name "Watch", :watch/slug nil}]
  (d/transact conn [watch]))

;; Create, Update, Retract
;; d/transact returns a "future", to wait for the result you can use @
;; recovering the temids
(let [calculator (d.product/build-product "Watch 2" "watch_2" 888.5M)
      result @(d/transact conn [calculator])
      entity-id (first (vals (:tempids result)))]
  (pprint entity-id)

  (pprint "update product price")
  (d/transact conn [[:db/add entity-id :product/price 99.9M]])

  (pprint "Retracting product slug")
  (d/transact conn [[:db/retract entity-id :product/slug]]))




(d.db/delete-database!)
(def conn
  (d.db/create-connection!))
(d.db/create_schema! conn)

(println "Inserting several products at once")
(let [item1 (d.product/build-product "Watch 1" "watch_1" 1.05M)
      item2 (d.product/build-product "Watch 2" "watch_2" 2.05M)
      item3 (d.product/build-product "Watch 3" "watch_3" 3.05M)
      item4 (d.product/build-product "Watch 4" "watch_4" 4.05M)]
  (println "Transacting the items")
  (let [result @(d/transact conn [item1 item2 item3 item4])]
    (pprint result))

  (println "Looking for the itens")
  (pprint (d.db/find-all-products (d/db conn)))
  (d.db/all-products-by-slug (d/db conn) "watch_1")

  )

(println "Find slugs")
(pprint (d.db/find-product-slugs (d/db conn)))

(println "Find product-name-and-slug")
(pprint (d.db/find-product-name-and-slug (d/db conn)))
(pprint (d.db/find-products-as-key-value (d/db conn)))
(pprint (d.db/find-products-pull (d/db conn)))
(pprint (d.db/find-products-all-fields (d/db conn)))

; Query the past: use d/as-of to find the database in a given time
(let [db (d/as-of (d/db conn) #inst "2021-10-15T18:37:39.175-00:00")]
  (pprint (d.db/find-products-all-fields db)))

; Filter by prices greater than a value
(pprint (d.db/find-product-with-minimum-price (d/db conn) 3M))


(println "Test \"many\" cardinality")
;; Redefine scheme with the new attribute
(d.db/create_schema! conn)
(let [product (d.product/build-product "Watch 1" "watch_1" 1.234M "key1")
      created-product @(d/transact conn [product])
      product-id (first (vals (:tempids created-product)))]

  (println "Created product" product-id)

  (d/transact conn [[:db/add product-id :product/key-word "key2"]
                    [:db/add product-id :product/key-word "key3"]])

  (println "After update 1")
  (pprint (last (d.db/find-products-all-fields (d/db conn))))

  (println "After update 2")
  (d/transact conn [[:db/retract product-id :product/key-word "key2"]
                    [:db/add product-id :product/name "Watch"]
                    [:db/add product-id :product/key-word "key2222"]])

  (pprint (last (d.db/find-products-all-fields (d/db conn)))))


(println "Find by one product by its db id")
(pprint (first (d.db/find-product-by-dbid (d/db conn) 17592186045424)))


(def conn (d.db/reset-db!))
;(println "Create random product")
(let [product (d.product/build-product-random)]
  (pprint product)
  (println "Creating random product with id" (str (:product/id product)))
  (d/transact conn [(d.product/build-product-random)])

  (println "Finding product by id (uuid)")
  (pprint (d.db/find-product-by-id (d/db conn) (:product/id product)))
  )

(println "Find product by uuid manually (YOU NEED TO UPDATE THE UUID IN SCRIPT)")
(pprint (d.db/find-product-by-id (d/db conn) #uuid"af2e16e5-adf5-46fa-8614-05ae91b2f9e9"))



(def conn (d.db/reset-db!))
(let [product1 (d.product/build-product-random)
      p1 (assoc product1 :product/key-word "A1")
      p2 (assoc product1 :product/key-word "A2")]


  (println "Saving p1")
  (d/transact conn [p1])

  (println "Get p1 from uuid")

  (pprint (d/pull (d/db conn) '[*] [:product/id (:product/id p1)]))

  (println "Print p2 before save: p1 and p2 has the same product/id, but different key-words")
  (pprint p2)

  (d/transact conn [p2])

  (println "Print p2 after save. As it has the same id, p1 was recreated adding the new key-word")
  (println "The key-word was defined with cardinality MANY!")
  (pprint (d/pull (d/db conn) '[*] [:product/id (:product/id p2)]))
  )