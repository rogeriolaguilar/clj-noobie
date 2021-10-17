(ns clj-noobie.datomic.product)

(defn- uuid [] (java.util.UUID/randomUUID))

(defn build-product
  ([name slug price]
  {:product/id  (uuid)
   :product/name  name
   :product/slug  slug
   :product/price price})
  ([name slug price key-word]
  {:product/id  (uuid)
   :product/name  name
   :product/slug  slug
   :product/price price
   :product/key-word key-word}))

(defn build-product-random []
  (let [name (str "Name " (rand 1000))
        slug (clojure.string/replace name #" " "_")
        price (bigdec (* (rand) 10000M))]
    (build-product name slug price)))
