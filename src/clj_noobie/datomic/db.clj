(ns clj-noobie.datomic.db
  (:use clojure.pprint)
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/ecommerce")

(defn create-connection []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn delete-database []
  (d/delete-database db-uri))

(defn create_schema [conn]
  (d/transact conn [{:db/ident       :product/name
                     :db/valueType   :db.type/string
                     :db/cardinality :db.cardinality/one
                     :db/doc         "Product name"}
                    {:db/ident       :product/slug
                     :db/valueType   :db.type/string
                     :db/cardinality :db.cardinality/one
                     :db/doc         "HTTP path to access the product"}
                    {:db/ident       :product/price
                     :db/valueType   :db.type/bigdec
                     :db/cardinality :db.cardinality/one
                     :db/doc         "Price of a product"}
                    {:db/ident       :product/key-word
                     :db/valueType   :db.type/string
                     :db/cardinality :db.cardinality/many}]))

(defn reset-db []
  (delete-database)
  (let [conn (create-connection)]
    (create_schema conn)))

(defn find-all-products [db]
  (d/q '[:find ?entity
         :where [?entity :product/name]] db))

; get one product id with the slug
; IMPORTANT: when use params in your query, notice things
; - We have to use ":in $ ?slug" in the query ($ represents db params)
; - We have to pass this params to the "d/q" function
; OBS: the $ in ":in $ ?slug" represents database passed to "d/q"
(defn all-products-by-slug [db slug]
  (let [query '[:find ?entity
                :in $ ?slug
                :where [?entity :product/slug ?slug]]]

    (println "Running query:" query)
    (d/q query db slug)))


;; When the variable is not used, we can use _
(defn find-product-slugs [db]
  (d/q '[:find ?any-slug
         :where [_ :product/slug ?any-slug]] db))

;; Here we must use the ?product, or the result will be different!!!
(defn find-product-name-and-slug [db]
  (d/q '[:find ?name ?slug
         :where [?product :product/name ?name]
         [?product :product/slug ?slug]] db))

;; Using the keys attribute to format the response
;; The keys names can be different of datom names
(defn find-products-as-key-value [db]
  (d/q '[:find ?name ?slug ?price
         :keys name slug price
         :where [?product :product/name ?name]
         [?product :product/slug ?slug]
         [?product :product/price ?price]] db))


(defn find-products-pull [db]
  (d/q '[:find (pull ?product [:product/name :product/slug :product/price])
         :where [?product :product/name]] db))

(defn find-products-all-fields [db]
  (d/q '[:find (pull ?product [*])
         :where [?product :product/name]] db))


; We can use functions in the :where list to define the filters
; WARN: The order matters!!!!
; Datomic executes the functions in the defined order.
; We define the execution plan!
; USE THE MOST RESTRICTIVE FILTER FIRST to improve performance
(defn find-product-with-minimum-price [db minimum-price]
  (d/q '[:find ?name ?slug ?price
         :in $ ?minimum-price
         :keys name slug price
         :where [?product :product/name ?name]
         [?product :product/slug ?slug]
         [?product :product/price ?price]
         [(> ?price ?minimum-price)]] db minimum-price))

