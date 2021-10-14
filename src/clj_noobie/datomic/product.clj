(ns clj-noobie.datomic.product)

(defn build_product
  [name slug price]
  {:product/name  name
   :product/slug  slug
   :product/price price})