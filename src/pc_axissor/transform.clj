(ns pc-axissor.transform
  (:require [com.rpl.specter :as sc]))

(defn get-value-specs [metadata]
  (->> metadata
       :values
       (map second)
       reverse))

(defn get-value-spec [ix-all value-spec-counts ix spec]
  (let [inc-every    (apply * (take ix value-spec-counts))
        my-count     (get value-spec-counts ix)
        change-count (int (/ ix-all inc-every))
        index (mod change-count my-count)]
    (get spec index)))

(defn get-value-and-desc [value-specs ix value]
  [(vec (map-indexed (partial get-value-spec
                              ix
                              (mapv count value-specs))
                     value-specs))
   value])

(defn tuples-with-all
  "Get pairs of tuple-of-labels + value from a parsed px file."
  [{:keys [data] :as parsed}]
  (let [vs (get-value-specs parsed)]
    (->> data
         (map-indexed (partial get-value-and-desc vs)))))

(defn map-relevant-labels
  "Map all labels that have more than one value in the dataset.

  `f` is a function that takes `vnames` (tuple of label names)
  and `label-value` which is the value of a label."
  [labels f values]
  (->> values
       reverse
       (map-indexed (fn [ix [vnames possible-values]]
                      (when (-> possible-values count (> 1))
                        (f vnames (nth labels ix)))))
       (filter some?)))
