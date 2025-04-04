(ns clj-noobie.tests.logic-test
  (:require [clojure.test :refer :all]
            [clj-noobie.tests.logic :refer :all]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.properties :as prop])
  (:import (clojure.lang ExceptionInfo)))

(deftest queue-has-space?-test
  (testing "there is space in the queue"
    (is (queue-has-space? {:waiting-room []} :waiting-room)))

  (testing "there queue is full"
    (is (not (queue-has-space? {:waiting-room [1 2 3 4 5]} :waiting-room))))

  (testing "there are more than the limit in the queue"
    (is (not (queue-has-space? {:waiting-room [1 2 3 4 5 6]} :waiting-room))))

  (testing "when the department do not exists"
    (is (not (queue-has-space? {:waiting-room [1 2 3]} :other-department))))

  (testing "GENERATOR: random queue with until 4 items"
    (doseq [queue (gen/sample (gen/vector gen/string-alphanumeric 0 4) 50)]
      (is (queue-has-space? {:waiting-room queue} :waiting-room)))))

(let [full-waiting-room {:waiting-room [1 2 3 4 5]}
      not-full-waiting-room {:waiting-room [1 2 3]}]
  (deftest check-in_test
    (testing "check-in if there is space"
      (is (= {:waiting-room [1 2 3 4]}
             (check-in not-full-waiting-room :waiting-room 4))))

    (testing "check-in when department do not exists"
      (is (= not-full-waiting-room
             (check-in not-full-waiting-room :not-existent-key :foo))))

    (testing "check-in when the department has not space"
      (is (thrown-with-msg? ExceptionInfo #"not space"
                            (check-in full-waiting-room :waiting-room 6))))

    (testing "check-in thrown error with info when the department has not space"
      (is (try
            (check-in full-waiting-room :waiting-room 6)
            false
            (catch ExceptionInfo e
              (= {:person 6 :department :waiting-room} (ex-data e))))))))


(defspec explore-generators 10                              ; 10 generative tests
         (prop/for-all
           [queue (gen/vector gen/string-alphanumeric 0 4)
            person-name gen/string-alphanumeric]
                       (println "HERE")
                       (println person-name queue)
                       true))