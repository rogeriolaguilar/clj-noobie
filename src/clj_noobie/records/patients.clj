(ns clj-noobie.records.patients
  (:import (com.datomic.lucene.search.payloads PayloadTermQuery)))

(defrecord Patient [id nome birthday])
(defrecord PatientHeathInsurance [id nome birthday plan])

(defprotocol Payer
  (payment-method [patient]))

(extend-type Patient
  Payer
  (payment-method [patient] :credit))

(extend-type PatientHeathInsurance
  Payer
  (payment-method [patient] :health-insurance))


(println (payment-method (->Patient 14 "Name1" "19/02/1991")))

(println (payment-method (map->Patient {:id 14 :name "Name1" :birthday "19/02/1991"})))

(println (-> (map->Patient {:id 14 :name "Name1" :birthday "19/02/1991"})
             (payment-method)))                             ; credit


(println (-> (map->PatientHeathInsurance {:id 14 :name "Name1" :birthday "19/02/1991"})
             (payment-method)))