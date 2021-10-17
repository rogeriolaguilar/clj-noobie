(ns clj-noobie.datomic.refs
  (:use [clojure.pprint])
  (:require
    [datomic.api :as d]
    [clj-noobie.datomic.product :as product]
    [clj-noobie.datomic.category :as category]
    [clj-noobie.datomic.db :as d.db]))

(println "Exploring Datomic data model using references")
(let [p1 (product/build-product-random)
      p1-id (:product/id p1)
      p2 (product/build-product-random)
      p2-id (:product/id p2)
      p3 (product/build-product-random)
      p3-id (:product/id p3)
      c1 (category/build-category-random)
      conn (d.db/reset-db)
      db-now (fn [] (d/db conn))]

  (println "Product before save")
  (pprint p1)

  (println "Category before save")
  (pprint c1)

  (println "Create product and category")
  (d/transact conn [p1 c1])

  (println "Find all products")
  (pprint (d/q '[:find (pull ?product [*])
                 :where [?product :product/name]] (db-now)))
  (println "Find all categories")
  (pprint (d/q '[:find (pull ?product [*])
                 :where [?product :category/name]] (db-now)))


  (println "Add category to the product")
  (d/transact conn [[:db/add
                     [:product/id p1-id]
                     :product/category
                     [:category/id (:category/id c1)]]])

  (pprint "Find product again")
  (let [product (d/pull (db-now) '[*] [:product/id p1-id])]
    (pprint product)
    ))