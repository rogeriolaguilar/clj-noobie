(ns clj-noobie.tests.logic-test
  (:require [clojure.test :refer :all]
            [clj-noobie.tests.logic :refer :all]))

(deftest queue-has-space?-test
  (testing "there is space in the queue"
    (is (queue-has-space? {:waiting-room []} :waiting-room)))

  (testing "there queue is full"
    (is (not (queue-has-space? {:waiting-room [1 2 3 4 5]} :waiting-room))))

  (testing "there are more than the limit in the queue"
    (is (not (queue-has-space? {:waiting-room [1 2 3 4 5 6]} :waiting-room))))

  (testing "when the department do not exists"
    (is (not (queue-has-space? {:waiting-room [1 2 3]} :other-department)))))

(deftest check-in_test
  (testing "check-in if there is space"
    (is (= {:waiting-room [1 2 3 4]}
           (check-in {:waiting-room [1 2 3]} :waiting-room 4))))

  (testing "check-in when department do not exists"
    (is (= {:waiting-room [1 2 3]}
           (check-in {:waiting-room [1 2 3]} :not-existent-key :foo))))

  (testing "check-in when the department has not space"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo #"not space"
                          (check-in {:waiting-room [1 2 3 4 5]} :waiting-room 6)))))

