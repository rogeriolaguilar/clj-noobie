(ns clj_noobie.logic
  (:require [clj_noobie.db :as n.db]))
; requiring db just keep the test functions

; grouping order by user
(group-by :user (n.db/all-orders))

(defn count-orders-by-user
  [[user orders]]
  {:user-id user
   :orders-count  (count orders)})

; count order by user
(map count-orders-by-user
     (group-by :user
               (n.db/all-orders)))
; or
(->> (n.db/all-orders)
     (group-by :user)
     (map count-orders-by-user))



(defn total-price-per-item
  [item]
  (* (get item :qtd 0) (get item :unit-price 0)))

; test
(total-price-per-item {:id :mochila, :qtd 2, :unit-price 80})
(total-price-per-item {:id :tenis, :qtd 1})


(defn total-price-per-order
  [order]
  (->>
    (vals order)
    (map total-price-per-item)
    (reduce +)))

; test
(total-price-per-order {:mochila  {:id :mochila, :qtd 2, :unit-price 80},
                        :camiseta {:id :camiseta, :qtd 3, :unit-price 40},
                        :tenis    {:id :tenis, :qtd 1}})

(defn total-price
  [orders]
  (->> orders
       (map :items)
       (map total-price-per-order)
       (reduce +)))

; test
(total-price [{:user 15,
               :items {:mochila {:id :mochila, :qtd 2, :unit-price 80},
                       :camiseta {:id :camiseta, :qtd 3, :unit-price 40},
                       :tenis {:id :tenis, :qtd 1}}}
              {:user 15,
               :items {:mochila {:id :mochila, :qtd 3, :unit-price 80},
                       :camiseta {:id :camiseta, :qtd 3, :unit-price 40},
                       :tenis {:id :tenis, :qtd 1}}}])


(defn orders-and-total-price-by-user
  [[user orders]]
  {:user-id user
   :orders-count (count orders)
   :total-price (total-price orders)})

; test
(->> (n.db/all-orders)
     (group-by :user)
     (map orders-and-total-price-by-user))


(defn user-resume
  [orders]
  (->> orders
       (group-by :user)
       (map orders-and-total-price-by-user)))

; test
(user-resume (n.db/all-orders))