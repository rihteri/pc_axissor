(ns pc-axissor.core
  (:require [pc-axissor.parser :as parser]
            [pc-axissor.transform :as tf]))

(def default-charset "iso-8859-15")

(defn group-data [data]
  (tf/group-all data))

(defn read-px [reader-or-file]
  (cond
    (string? reader-or-file)
    (with-open [reader (clojure.java.io/reader reader-or-file
                                               :encoding default-charset)]
      (parser/parse (line-seq reader)))
    (isa? (type reader-or-file) java.io.BufferedReader)
    (parser/parse (line-seq reader-or-file))
    (seq? reader-or-file)
    (parser/parse reader-or-file)
    :default
    nil))

(defn read-and-group [reader-or-file]
  (-> reader-or-file
      read-px
      group-data))
