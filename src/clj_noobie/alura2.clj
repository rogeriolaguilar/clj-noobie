(ns clj-noobie.alura2)

; TAIL RECURSION
(println "; TAIL RECURSION")
; without optimization
(defn print-all-1
  [seq]
  (when (next seq)
    (println (first seq))
    (print-all-1 (rest seq))))

(print-all-1 (range 10))

; susceptible to Stack overflow
;(print-all-1 (range 10000))
;4541
;Execution error (StackOverflowError) at nrepl.bencode/eval304$fn (bencode.clj:394).
;null
;4542

; using recursion optimization "recur"
(defn print-all-2
  [seq]
  (when (next seq)
    (println (first seq))
    (recur (rest seq))))
;(print-all-2 (range 10000))

; function with more than one contract
(defn count-all
  ([total seq]
    (if (not (empty? seq))
      (recur (inc total) (rest seq))
      total))
  ([seq] (count-all 0 seq)))


(count-all 0 [1 2 3 4])
(count-all 0 [])
(count-all [1 2 3 4])
(count-all [])

; recur with loop
; It not a good practice to use it
(println "; recur with loop")
(defn count-all-loop
  [sequence]
  (loop [total 0
         current-seq sequence]
    (if ((comp not empty?) current-seq)
      (recur (inc total) (next current-seq))
      total)))

(println (count-all-loop [1 2 3 4]))
(count-all-loop [])

