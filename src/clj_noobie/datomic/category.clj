(ns clj-noobie.datomic.category)

(defn- uuid [] (java.util.UUID/randomUUID))

(defn build-category
  ([name]
  {:category/id  (uuid)
   :category/name  name}))

(defn build-category-random []
    (build-category (str "Name " (rand 1000))))
