(ns pc-axissor.parser
  (:require [pc-axissor.read-line :as rl]
            [pc-axissor.read-value :as rv]
            [clojure.string :as string]
            [com.rpl.specter :as sc]))

(defn join-lines [lines]
  (let [[line other-lines] (split-with #(nil? (re-find #";$" %)) lines)]
    (into [(string/join (into (vec line)
                              (first other-lines)))]
          (when (not (empty? (rest other-lines)))
            (join-lines (rest other-lines))))))

(defn extract-subkey [full-key]
  (second full-key))

(defn no-subkey? [full-value]
  (-> full-value first nil?))

(defn extract-value [full-value]
  (-> full-value second))

(defn parse-meta [metadata]
  (->> metadata
       join-lines
       (map rl/read-meta)
       (filter some?)
       (group-by #(-> % first first))
       (sc/transform [sc/MAP-VALS sc/ALL sc/FIRST] extract-subkey)
       (sc/transform [sc/MAP-VALS sc/ALL no-subkey?] extract-value)))

(defn parse-data [data]
  (doall (->> data
              (map #(string/replace % #"\s?\;\s?$" ""))
              (map #(string/split % #" "))
              (apply concat)
              (filter #(not (empty? %)))
              (map rv/read-value))))

(defn parse [lines]
  (let [[metadata data] (->> lines
                             (split-with #(not= "DATA=" %)))
        parsed-metadata (parse-meta metadata)]
    (-> parsed-metadata
        (assoc :data (parse-data (rest data))))))
