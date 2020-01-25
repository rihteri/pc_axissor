(defproject pc_axissor "0.1.0-SNAPSHOT"
  :description "Library for reading PC Axis statistics files."
  :url "https://github.com/rihteri/pc_axissor"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [camel-snake-kebab "0.4.1"]
                 [com.rpl/specter "1.1.3"]]
  :main ^:skip-aot pc-axissor.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
