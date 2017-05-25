(ns pc-axissor.utils
  (:require [clojure.string :as string]))

(defn get-quoted-string [qs]
  (when qs
    (-> qs
        (string/replace #"^\"" "")
        (string/replace #"\";$" ""))))

(defn get-string-from-line [line]
  (-> line
      (string/split #"=" 2)
      second
      get-quoted-string))

(def string-array-separator #"\",\"")

(defn get-strings-from-line [line]
  (-> line
      get-string-from-line
      (string/split string-array-separator)))

(defn get-key-part [line]
  (-> line
      (string/split #"=" 2)
      first))

(defn get-key-from-line [line]
  (-> line
      get-key-part
      (string/split #"[\(\[]" 2)
      first))

(defn contains-lang? [line]
  (->> (string/split line #"=" 2)
       first
       (re-find #"\[[a-z]*\]")))

(def main-key-with-sub-key-start #"^[A-Z]*\(\"")

(defn get-sub-key-from-line [line]
  (let [kp (get-key-part line)]
    (when (re-find main-key-with-sub-key-start kp)
      (-> kp
          (string/replace main-key-with-sub-key-start "")
          (string/replace #"\"\)$" "")
          (string/split string-array-separator)))))
