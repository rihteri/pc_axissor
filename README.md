# pc_axissor

This is a library for reading PC-Axis statistics database files, as available from e.g. [stat.fi](http://www.stat.fi/org/avoindata/pxweb.html).

Resources on the file format:
* [Px suite on stat.fi](https://www.stat.fi/tup/tilastotietokannat/px-tuoteperhe_en.html)
* [Statistical programs for Px-files on scb.se](https://www.scb.se/en/services/statistical-programs-for-px-files/).

## Functions

### read-px
Produces a clojure map of the contents of a .px file. This contains the keys that are present in the px-file itself. Only the values in the default language of the dataset are imported for now.

The format for descriptions is a vector of tuples, where a single-value tuple is a description related to the whole dataset and a pair is a description of a single "column", where the column name is the first element and the description itself is the second element.

The dataset itself can be found as BigDecimals and keywords under the key `:data` - see below for how to turn this into a structured representation.

A value can be one of the following special keyword-values: `:not-logical`, `:not-available`, `:non-disclosure`, `:none`, `:reserved`, `:empty` or `:none`.

### samples
Given a data file parsed with `read-px`, return a sequence of maps that each contains a single sample and the relevant labels. Only relevant labels - meaning such labels for which there are more than a single value within the data set - are included.

```
({"Some label" "Value"
  "Other label" "Value of the other label"
  "Name of main variable" 42M}
  ;; ...
 {"Some label" "Value"
  "Other label" "Another value for the other label"
  "Name of main variable" :not-logical})
```

### tuples
Given a data file parsed with `read-px`, retun a sequence of tuples that contain the labels and a single values, in the order that they appear in the file, omitting redundant labels (i.e. those for which only a single distinct value is present in the file).

```
(["Value" "Value of the other label" 42M]
 ;; ...
 ["Value" "Another value for the other label" :not-logical])
```

## Usage
The library is not yet in clojars or such, so refer to a git hash - example with `deps.edn`:

```
{github-rihteri/pc_axissor {:git/url "https://github.com/rihteri/pc_axissor.git"
                            ;; check latest commit hash!
                            :sha     "796ac1ba998523cbcaa4cff62295448dd818298a"}}
```

Download some data from e.g. stat.fi. To read a .px file into a map, use `pc-axissor.core/read-px`.

```
(require '[pc-axissor.core :as px])

(def parsed (px/read-px "xyz.px")) ; can aso be java.io.BufferedReader
```

The metadata of the file can be inspected by reading the keys
```
(keys parsed)
;; => (:description :title :source :note :values ... :data)

(:note parsed)
;; => [["... this is a note relating to the whole dataset"]
;;     ["Name of some column" "this is a note relating to a single column of the data"]
;;     ...]
```

The data contents of the file are under the keyword `:data`

```
(:data parsed) ; a sequence of BigDecimals and keywords
;; => (123M 234M :not-logical 42M)
```

Getting structured data out

```
(px/samples parsed)
;; => ({"Key" "Value" "Variable" 123M} ...)
```

or

```
(px/tuples parsed)
;; => (["Value" 123M] ...)
```

Drawing graphs with [oz](https://github.com/metasoarous/oz)

```
(require '[oz.core :as oz])

;; example here is statistic 111k of stat.fi, greenhouse gas emissions of Finland
(oz/view! {:data     {:values (->> (px/samples parsed)
                                   ;; remove special values - those cannot be plotted
                                   (remove #(-> % (get "Kasvihuonekaasupäästöt Suomessa") keyword?))
                                   ;; convert BigDecimals to integers for the value field
                                   (map #(update % "Kasvihuonekaasupäästöt Suomessa"
                                                 (fn [v] (.intValue v)))))}
           :encoding {:x     {:field "Vuosi" ; year field
                              :type  "ordinal"}
                      :y     {:field "Kasvihuonekaasupäästöt Suomessa" ; the value field
                              :type  "quantitative"}
                      :color {:field "Päästöluokka" ; another grouping label
                              :type  "nominal"}}
           :mark     "bar"})
```

## License

Copyright © 2017 Vincit 
Copyright © 2020 Riku Keski-Keturi

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
