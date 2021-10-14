(ns clj-noobie.datomic.db
  (:use clojure.pprint)
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/ecommerce")

(defn create-connection []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn delete-database []
  (d/delete-database db-uri))

(def product_schema )

(defn create_schema [conn]
  (d/transact conn [{:db/ident :product/name
                      :db/valueType :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc "Product name"}
                     {:db/ident :product/slug
                      :db/valueType :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc "HTTP path to access the product"}
                     {:db/ident :product/price
                      :db/valueType :db.type/bigdec
                      :db/cardinality :db.cardinality/one
                      :db/doc "Price of a product"}]))