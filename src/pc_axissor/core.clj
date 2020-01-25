(ns pc-axissor.core
  (:require [pc-axissor.parser :as parser]
            [pc-axissor.transform :as tf]))

(def default-charset "iso-8859-15")

(defn read-px
  "Read a .px file. Input can be a filename or `java.io.BufferedReader`, or a
  sequence of string that each represent a line."
  [reader-or-file]
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

(defn samples
  "Add key `:data-items` which contains a sequence of maps
  suitable for e.g. `oz.core` plots. All `:labels` that have
  more than one value are in each map as label -> value, and
  the primary variable of the data set is under the key
  specified in `:contents`."
  [{:keys [values contents] :as parsed}]
  (let [val-key (ffirst contents)]
    (->> parsed
         tf/tuples-with-all
         (map (fn [[labels value]]
                (->> values
                     (tf/map-relevant-labels labels
                                             (fn [[vname] label-value]
                                               [vname label-value]))
                     (into {val-key value})))))))

(defn tuples
  "Get tuples with the relevent labels (as in, more than one value
  present in the file for the label) and the value as the last item.

  See also `tuples-with-all`"
  [{:keys [values] :as parsed}]
  (->> parsed
       tf/tuples-with-all
       (map (fn [[labels value]]
              (-> (tf/map-relevant-labels labels
                                          (fn [_ label-value] label-value)
                                          values)
                  vec
                  (conj value))))))
