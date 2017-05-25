(ns pc-axissor.transform)

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

(defn group-all [data]
  (let [vs (get-value-specs data)]
    (assoc data
           :data
           (->> data
                :data
                (map-indexed (partial get-value-and-desc vs))))))
