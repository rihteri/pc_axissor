(ns pc-axissor.read-value)

(defn read-value [value]
  (case value
    "\".\""      :not-logical
    "\"..\""     :not-available
    "\"...\""    :non-disclosure
    "\"....\""   :none
    "\".....\""  :reserved
    "\"......\"" :empty
    "\"-\""      :none
    (try (BigDecimal. value)
         (catch java.lang.NumberFormatException e
             nil))))
