(ns clj-noobie.datomic.product
  (:require [schema.core :as s]
            [clj-noobie.datomic.category :as category])
  (:import (java.util UUID)))

(defn- uuid [] (UUID/randomUUID))

(def Product
  {:product/id                        UUID
   :product/name                      s/Str
   :product/slug                      s/Str
   :product/price                     BigDecimal
   (s/optional-key :product/key-word) [s/Str]
   (s/optional-key :product/category) category/Category})

(defn build-product
  ([name slug price]
   {:product/id    (uuid)
    :product/name  name
    :product/slug  slug
    :product/price price})
  ([name slug price key-word]
   {:product/id       (uuid)
    :product/name     name
    :product/slug     slug
    :product/price    price
    :product/key-word key-word}))

(defn build-product-random []
  (let [name (str "Name " (rand-int 1000))
        slug (clojure.string/replace name #" " "_")
        price (bigdec (* (rand) 10000M))]
    (build-product name slug price)))
