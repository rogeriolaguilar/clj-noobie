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
      c1 (category/build-category "c1")
      c2 (category/build-category "c2")
      conn (d.db/reset-db!)
      db-now (fn [] (d/db conn))]

  (println "Product p1 before save")
  (pprint p1)
  (println "Category c1 before save")
  (pprint c1)

  (println "Create p1 and c1")
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
    )


  (println "###### Associate category to the product before save the product")
  (println "(Must the category already exists???)")
  (let [p2 (assoc p2 :product/category c2)]
    @(d/transact conn [p2])

    (println "Get p2 from DB")
    (-> (d/pull (db-now) '[*] [:product/id (:product/id p2)])
        (pprint))

    (println "Get c2 from DB")
    (-> (d/pull (db-now) '[*] [:category/id (:category/id c2)])
        (pprint))

    (println "### Must the category already exists? No, when the product was saved, it also has saved the related category")
    )

  (println "###### Find product and related category names")
  (-> (d/q '[:find ?p_name ?c_name
             :where
             [?product :product/name ?p_name]
             [?product :product/category ?category]
             [?category :category/name ?c_name]]
           (db-now))
      (pprint))

  (println "###### Find product and related category names adding keys")
  (-> (d/q '[:find ?p_name ?c_name
             :keys product category
             :where
             [?product :product/name ?p_name]
             [?product :product/category ?category]
             [?category :category/name ?c_name]]
           (db-now))
      (pprint))


  (println "Save p3 with category c2")
  (let [p3 (assoc p3 :product/category c2)]
    (d/transact conn [p3])

    (println "Get p3 from db")
    (pprint (d/pull (db-now) '[*] [:product/id (:product/id p3)]))
    (println "Get c2 from db")
    (pprint (d/pull (db-now) '[*] [:category/id (:category/id c2)]))
    )

  (pprint c2)
  (println "Get all products with category = c2  (FORWARD NAVIGATION)")
  (pprint (d/q '[:find (pull ?product [:product/name {:product/category [:category/name]}])
                 :in $ ?category-id
                 :where
                 [?category :category/id ?category-id]
                 [?product :product/category ?category]]
               (db-now)
               (:category/id c2)
               ))

  (println "##### Get all products with category = c2  (BACKWARD NAVIGATION)")
  (pprint (d/q '[:find (pull ?category [:category/name {:product/_category [:product/name :product/slug]}])
                 :in $ ?category-id
                 :where
                 [?category :category/id ?category-id]]
               (db-now)
               (:category/id c2)
               ))


  (println "All products")
  (pprint p1)
  (pprint p2)
  (pprint p3)
  (println "Save p4! one more product with same p3 price")
  (let [p4 (assoc (product/build-product-random) :product/price (:product/price p3))]
    (pprint p4)
    (d/transact conn [p4])
    )


  (println "AGGREGATION: Find min, max prices and count (count is wrong)!!")
  (println "WARN: if two product has the same price, count will count just one in this query")
  (pprint (d/q '[:find (min ?price) (max ?price) (count ?price)
                 :keys min-price max-price count
                 :where [_ :product/price ?price]],
               (db-now)))


  (println "AGGREGATION: Find min, max prices and count (COUNT FIXED)")
  (pprint (d/q '[:find (min ?price) (max ?price) (count ?id)
                 :keys min-price max-price count
                 :where [?entity :product/price ?price]
                 [?product :product/id ?id]],
               (db-now)))


  (println "AGGREGATION: Find min, max prices and count BY CATEGORY")
  (pprint (d/q '[:find ?cname (min ?price) (max ?price) (count ?id) (sum ?price)
                 :keys category min-price max-price count sum-price
                 :where [?entity :product/price ?price]
                 [?product :product/id ?id]
                 [?product :product/category ?category]
                 [?category :category/name ?cname]],
               (db-now)))

  (println "NESTED QUERIES: The products with the most expensive price")
  (d/q '[:find (pull ?product [*])
         :where [(q '[:find (max ?price)
                      :where [_ :product/price ?price]]
                    $) [[?price]]]
         [?product :product/price ?price]]
       (db-now))

  )