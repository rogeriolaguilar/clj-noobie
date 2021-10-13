(ns clj_noobie.basics.alura3
  (:require [clj_noobie.db :as n.db]
            [clj_noobie.logic :as l.logic]))

(println (n.db/all-orders))

(l.logic/user-resume (n.db/all-orders))


(let [orders (n.db/all-orders)
      resume (l.logic/user-resume orders)]
  (println "Resume" resume)
  (println "Order by price" (sort-by :total-price resume))
  (println "Order by price reversed" (reverse (sort-by :total-price resume)))

  (println "Orders" orders)
  (println "Get-in sample:" (get-in orders [0 :items :mochila :qtd]))

  (println "Resume filtering more than $600:" (filter #(> (:total-price %) 600) resume))
  (println "Resume some more than $600:" (some #(> (:total-price %) 600) resume))
  )