(ns clj-noobie.hlogic)

(defn check-in
  [hospital queue-name name]
  (println "Try to insert" name)
  (let [queue (get hospital queue-name)
        queue-size (count queue)
        max-size? (>= queue-size 5)]

    ;(println "Queue size" queue-size "Max size?" max-size?)
    (Thread/sleep (rand 100))
    (if max-size?
      (do
       (println ">>>> Queue" queue-name "reach max size" queue-size)
       hospital
       )
      (do
        (println "Inserting" name)
        (update hospital queue-name conj name))
      )))


(defn resolve
  [hospital queue-name]

  (update hospital queue-name pop)

  )