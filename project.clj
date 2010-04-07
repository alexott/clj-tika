(defproject clj-tika "1.0.0-SNAPSHOT"
  :description "Interface to Apache Tika"
  :dependencies [[org.clojure/clojure "1.1.0"]
                 [org.clojure/clojure-contrib "1.1.0"]
                 [org.apache.tika/tika-parsers "0.7"]
                 ]
  :dev-dependencies [[leiningen/lein-swank "1.2.0-SNAPSHOT"]]
  :repositories {"apache-releases" "https://repository.apache.org/content/repositories/releases/"}
  )
