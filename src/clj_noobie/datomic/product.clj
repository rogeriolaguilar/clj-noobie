(ns clj-noobie.datomic.product)

(defn build_product
  ([name slug price]
  {:product/name  name
   :product/slug  slug
   :product/price price})
  ([name slug price key-word]
  {:product/name  name
   :product/slug  slug
   :product/price price
   :product/key-word key-word}))