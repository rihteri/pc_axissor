(ns pc-axissor.read-line
  (:require [clojure.string :as string]
            [pc-axissor.utils :as utils]
            [camel-snake-kebab.core :as csk]))

(defmulti read-meta utils/get-key-from-line)

(defmethod read-meta "CHARSET" [line]
  [[:charset nil]
   (utils/get-string-from-line line)])

(defmethod read-meta :default [line]
  (when (not (utils/contains-lang? line))
    (let [key     (csk/->kebab-case-keyword (utils/get-key-from-line line))
          sub-key (utils/get-sub-key-from-line line)]
      [[key
        sub-key]
       (utils/get-strings-from-line line)])))
