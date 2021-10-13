(ns clj-noobie.basics.alura1)

; Vectors
(def prices [30 700 1000])

(println (prices 0))
(println (get prices 0))
(println (prices 2))
;(println (prices 3))                                        ; error
(println (get prices 3 999))                                ; 999

(println (conj prices 123))

; All expressions bellow return the same result
(println (update prices 0 #(+ % 1 )))
(println (update prices 0 (fn [elem] (+ elem 1))))
(println (update prices 0 (fn [elem] (inc elem))))
(println (update prices 0 #(inc %)))
(println (update prices 0 inc))

; map, filter, reduce
(map inc prices)
(filter even? (range 0 10))
(filter odd? (range 0 10))
(reduce + prices)                                           ; sum all prices
(reduce + 10 prices)                                        ; init value is 10

; key values HashMaps
(def my-map { :a 1 :b 2})
(my-map :a)                                                 ; it can raise error if the map is nil
(:a my-map)
(my-map :not-existent-key)                                  ; returns nil
(my-map :not-existent-key :default-value)                   ; returns default-value

(assoc my-map :c 3)
(update my-map :b inc)
(dissoc my-map :a)

; #### THREADING ( changing the execution order of the functions (encadeando funcoes))
; Get a from my-map, sum 3 than multiply by two
; (1 + 3) * 2 = 8
; * if wrong order: (1 * 2) + 3 = 5

; without threading
(* 2 (+ 3
       (:a my-map)))                                        ; 8

; with threading!!!
(-> my-map
    :a
    (+ 3)
    (* 2))


; Another THREADING sample
(def
  clients {
            :collections {
                 :name "Card Brands"
                 :brands ["Viza" "Mister" "ELLO" "Amecs"] } })

; counting card brands
(-> clients :collections :brands count)


; DESTRUCTING
(def my-map {:a1 {:c 1 :b 1}
             :a2 {:c 2 :b 2}})

(defn print-class [elem]
  (println "Elem" elem "class is" (class elem)))

(print-class my-map)
;Elem {:a1 {:c 1, :b 1}, :a2 {:c 2, :b 2}} class is clojure.lang.PersistentArrayMap

(map print-class my-map)
;Elem [:a1 {:c 1, :b 1}] class is clojure.lang.MapEntry
;Elem [:a2 {:c 2, :b 2}] class is clojure.lang.MapEntry


; function using destruction of the vector using [[v1 v2]]
(defn return-values
  [[_key value]]
  value)

(map return-values my-map)
;=> ({:c 1, :b 1} {:c 2, :b 2})

; EXAMPLE: Creating function to calculate total amount in the shopping card
; calculating expected amount manually
;(+ (* 100 300) (* 2000 30) (* 3000 3)) ; result: 99000

(defn price-per-item
  [[_key item]]
  (* (:price item) (:qtd item)))

; test
(price-per-item [:pen {:price 100 :qtd 300}])

(defn total-price
  [items]
  (println items)
  (reduce + (map price-per-item items)))

; same function using THREAD LAST (->>)
(defn total-price
  [items]
  (->> items
      (map price-per-item)
      (reduce +)))

; Threading with "->" append argument in the first position
; Threading with "->>" append argument as the last position


(let [shopping-card {:pen      {:price 100 :qtd 300}
                     :notebook {:price 2000 :qtd 30}
                     :book     {:price 3000 :qtd 3}}]

  (println shopping-card)
  (total-price shopping-card))


; vals - Get the value from a map
(vals {:a {:k1 :v1} :b {:k2 :v2}})
; => ({:k1 :v1} {:k2 :v2})

(vals {:k1 :v1, :k2 :v2})
; => (:v1 :v2)


; Sample 2
; filtering free products

(def shopping-card {:pen      {:price 0 :qtd 300}
                      :notebook {:qtd 30}
                      :book     {:price 3000 :qtd 3}})

(defn free?
  [item]
  (println "free item?" item)
  (<= (:price item 0) 0))

(filter free? (vals shopping-card))


(defn paid?
  [item]
  (not (free? item)))

(filter paid? (vals shopping-card))

;; FUNCTION COMPOSITION: Using "comp" to create the same function
(comp not free?)
(filter (comp not free?) (vals shopping-card))

