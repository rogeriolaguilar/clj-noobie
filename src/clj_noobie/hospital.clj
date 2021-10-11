(ns clj_noobie.hospital
  (:use [clojure pprint])
  (:require [clj_noobie.hmodel :as h.model]
            [clj-noobie.hlogic :as h.logic]))


(defn simulate-hospital []
  (def hospital (h.model/new-hospital))

  (def hospital (h.logic/check-in hospital :waiting-room "Person 1"))
  (def hospital (h.logic/check-in hospital :waiting-room "Person 2"))
  (def hospital (h.logic/check-in hospital :waiting-room "Person 3"))
  (def hospital (h.logic/check-in hospital :waiting-room "Person 4"))
  (def hospital (h.logic/check-in hospital :waiting-room "Person 5"))
  (def hospital (h.logic/check-in hospital :waiting-room "Person 6"))
  (def hospital (h.logic/check-in hospital :waiting-room "Person 7"))

  (def hospital (h.logic/resolve hospital :waiting-room))
  hospital
  )
; run to  test
(pprint (simulate-hospital))


(defn redefine-hospital [value]
  (def hospital (h.logic/check-in hospital :waiting-room value)))

(defn simulate-concurrent-call
  []
  (def hospital (h.model/new-hospital))

  (.start (Thread. (fn [] (redefine-hospital "Oi 1"))))
  (.start (Thread. (fn [] (redefine-hospital "Oi 2"))))
  (.start (Thread. (fn [] (redefine-hospital "Oi 3"))))
  (.start (Thread. (fn [] (redefine-hospital "Oi 4"))))
  (.start (Thread. (fn [] (redefine-hospital "Oi 5"))))
  (.start (Thread. (fn [] (redefine-hospital "Oi 6"))))
  (.start (Thread. (fn [] (redefine-hospital "Oi 7"))))
  (.start (Thread. (fn [] (redefine-hospital "Oi 8"))))
  (.start (Thread. (fn [] (redefine-hospital "Oi 9"))))
  (.start (Thread. (fn [] (redefine-hospital "Oi 10"))))

  (Thread/sleep (* 11 1000))
  hospital
  )

;(pprint (simulate-concurrent-call))


; ####### ATOM and SWAP

(defn test-atom
  []
  (let [hospital-atom (atom (h.model/new-hospital))]
    (println hospital-atom)


    (println ">>> Initial ATOM")
    (pprint hospital-atom)

    (println ">>> DEREF the ATOM")
    (pprint (deref hospital-atom))

    (println " >>> Checking in waiting-room. It returns a new hospital.")
    (pprint (h.logic/check-in (deref hospital-atom) :waiting-room "Person 1"))

    (println ">>> But it does not change the atom!")
    (pprint (deref hospital-atom))

    (println ">>> Changing data in the atom with swap!")
    (swap! hospital-atom h.logic/check-in :waiting-room "Person Swap 1")
    (swap! hospital-atom h.logic/check-in :waiting-room "Person Swap 2")
    (pprint hospital-atom)

    (println ">>> FINAL hospital")
    (pprint (deref hospital-atom))))

(test-atom)