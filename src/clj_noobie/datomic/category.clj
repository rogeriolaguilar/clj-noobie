(ns clj-noobie.datomic.category
  (:require [schema.core :as s])
  (:import [java.util UUID]))

(defn- uuid [] (UUID/randomUUID))

(def Category
  {:category/name s/Str
   :category/id UUID })

(defn build-category
  ([name]
  {:category/id  (uuid)
   :category/name  name}))

(defn build-category-random []
    (build-category (str "Name " (rand 1000))))
