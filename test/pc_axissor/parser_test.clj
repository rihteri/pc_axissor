(ns pc-axissor.parser-test
  (:require [pc-axissor.parser :as parser]
            [clojure.test :as t]))

(t/deftest line-joining
  (t/is (= ["a,b;" "c;"]
           (parser/join-lines ["a," "b;" "c;"]))))
