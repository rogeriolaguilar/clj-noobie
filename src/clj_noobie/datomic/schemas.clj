(ns clj-noobie.datomic.schemas
  (:use [clojure.pprint])
  (:require
    [datomic.api :as d]
    [clj-noobie.datomic.product :as product]
    [clj-noobie.datomic.db :as db]
    [clj-noobie.datomic.category :as category]
    [schema.core :as s]))

(let [conn (db/reset-db!)
      product (product/build-product-random)
      category (category/build-category-random)
      p2 (assoc product :product/category category)
      db-now #(d/db conn)]

  (pprint product)
  (pprint p2)

  (s/validate product/Product p2)
  (d/transact conn [p2])
  (pprint db-now)
  )