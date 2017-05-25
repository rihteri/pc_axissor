(ns pc-axissor.utils-test
  (:require [pc-axissor.utils :as utils]
            [clojure.test :as t]))

(t/deftest get-qs
  (t/is (= "ugh" (utils/get-quoted-string "\"ugh\";")))
  (t/is (= "u\"gh" (utils/get-quoted-string "\"u\"gh\";"))))

(t/deftest get-subkey
  (t/is (= ["aaa"] (utils/get-sub-key-from-line "XXX(\"aaa\")=asdf;")))
  (t/is (nil? (utils/get-sub-key-from-line "XXX=asdf;"))))
