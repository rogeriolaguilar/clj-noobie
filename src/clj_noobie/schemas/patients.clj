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
  {:id   id
   :name name})

(pprint (new-patient 15 "AAA"))
;(pprint (new-patient "33" "AAA")); raise error


(println "Defining scheme for Patients")
(def Patient
  "Schema of Patients"
  {:id s/Num :name s/Str})

(println "testing the schema")
(pprint (s/validate Patient {:id 15 :name "RRR"}))
; (pprint (s/validate Patient {:id 15})) ; raise error
; (pprint (s/validate Patient {:id 15 :name "RRR" :new-key "AAA"})) ; raise error

(println "using the Patient schema to define a function return")
(s/defn new-patient :- Patient
  [id :- s/Num, name :- s/Str]
  {:id id :name name})


(println "Define schema with some logic (using s/pred)")
(s/defn positive-number? [number] (> number 0))
(def PositiveNumber (s/pred positive-number?))

; (pprint (s/validate PositiveNumber -1)) raise error
(pprint (s/validate PositiveNumber 43))

(println "Defining scheme for Patients again, but composing schema validations")
(def Patient
  "Schema of Patients"
  {:id (s/constrained s/Int pos?) :name s/Str})

(pprint (s/validate Patient {:id 42 :name "RRR"}))
; (pprint (s/validate Patient {:id -42 :name "RRR"}))         ; raise error

;; Do not use lambdas in schemas!!! The error when using lambdas are not properly logged

(println "Define schema for vectors")
(def Numbers [PositiveNumber])
(pprint (s/validate Numbers [1 2 3 4]))
(pprint (s/validate Numbers []))
(pprint (s/validate Numbers nil))
;(pprint (s/validate Numbers [1 2 3 -4]))                    ; raise error


(println "Define schema with optional keys (By default, keys are required in Schema)")
(def Patient
  "Schema of Patients"
  {:id                   (s/constrained s/Int pos?)
   :name                 s/Str
   (s/optional-key :age) s/Int})
(pprint (s/validate Patient {:id 42 :name "RRR"}))
(pprint (s/validate Patient {:id 42 :name "RRR" :age 21}))
;(pprint (s/validate Patient {:id 42 :name "RRR" :age 21 :invalid-key "232"}))

(println "Define schema for the key of a map")
(def Id-Patient
  {s/Int Patient})
(pprint (s/validate Id-Patient {42 {:id 42 :name "RRR"}}))
; (pprint (s/validate Id-Patient {:id {:id 42 :name "RRR"}})) ; raise error
