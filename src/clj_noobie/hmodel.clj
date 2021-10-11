(ns clj_noobie.hmodel
  (:use [clojure.pprint]))


(let [queue clojure.lang.PersistentQueue/EMPTY]
  (pprint queue)
  (pprint (conj queue 1 2 3))
  (pprint (conj (conj queue 1 2 3) 4))
  (pprint (pop (conj queue 1 2 3)))
  (pprint "fim" )
  )


(defn new-hospital []
  {
    :waiting-room clojure.lang.PersistentQueue/EMPTY,
    :laboratory-1 clojure.lang.PersistentQueue/EMPTY,
    :laboratory-2 clojure.lang.PersistentQueue/EMPTY,
    :laboratory-3 clojure.lang.PersistentQueue/EMPTY
   })
