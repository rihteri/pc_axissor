(ns pc-axissor.transform-test
  (:require [pc-axissor.transform :as tf]
            [clojure.test :as t]))

(def vs [["a" "b"]
         ["A" "B"]])

(def counts (mapv count vs))

(t/deftest get-spec
  (t/is (= "a"
           (tf/get-value-spec 0 counts 0 (first vs))))
  (t/is (= "b"
           (tf/get-value-spec 1 counts 0 (first vs))))
  (t/is (= "A"
           (tf/get-value-spec 0 counts 1 (second vs))))
  (t/is (= "A"
           (tf/get-value-spec 1 counts 1 (second vs))))
  (t/is (= "B"
           (tf/get-value-spec 2 counts 1 (second vs))))
  (t/is (= "a"
           (tf/get-value-spec 4 counts 0 (first vs)))))

(t/deftest value-get
  (t/is (= [["a" "A"] :v]
           (tf/get-value-and-desc vs 0 :v)))
  (t/is (= [["a" "A"] :v]
           (tf/get-value-and-desc vs 4 :v)))
  (t/is (= [["b" "A"] :v]
           (tf/get-value-and-desc vs 1 :v))))
