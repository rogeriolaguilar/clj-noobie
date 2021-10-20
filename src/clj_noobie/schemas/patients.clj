(ns clj-noobie.schemas.patients
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(defn add-patient
  [patients, patient]
  (if-let [id (:id patient)]
    (assoc patients id patient)
    (throw (ex-info "Invalid patient without id" {:patient patient}))))

(defn add-visit
  [visits, patient, new-visits]
  (if (contains? visits patient)
    (update visits patient concat new-visits)
    (assoc visits patient new-visits)))


(defn test-add-patient []
  (let [p1 {:id 14, :name "Patient1"}
        p2 {:id 15, :name "Patient2"}
        p3 {:id 16, :name "Patient3"}
        patient-list (reduce add-patient {} [p1 p2 p3])]
    (pprint patient-list)))

(test-add-patient)

(pprint (s/validate Long 13))
;(pprint (s/validate Long ""))

(s/set-fn-validation! true)                                 ; ativate validation


; we have to use s/defn to create function with support of schemas
(s/defn simple-test [x :- Long]
  (println x))

;(simple-test "2") ; raise error

(s/defn new-patient
  [id :- Long
   name :- s/Str]
  {:id id
   :name name})

(pprint (new-patient 15 "AAA"))
;(pprint (new-patient "33" "AAA")); raise error
