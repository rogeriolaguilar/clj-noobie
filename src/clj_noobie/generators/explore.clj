(ns clj-noobie.generators.explore
  (:require [clojure.test.check.generators :as gen]))

(println (gen/sample gen/boolean 3))
(println (gen/sample gen/string 30))
(println (gen/sample gen/string-alphanumeric 30))
(println (gen/sample (gen/vector gen/big-ratio 3)))

; more sample here https://github.com/clojure/test.check/blob/master/doc/generator-examples.md