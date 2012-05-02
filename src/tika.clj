;; Clojure Interface to Apache Tika library
(ns tika
  (:import [java.io InputStream File FileInputStream]
           [java.net URL]
           [org.apache.tika.parser Parser AutoDetectParser ParseContext]
           [org.apache.tika.language LanguageIdentifier]
           [org.apache.tika.metadata Metadata]
           [org.apache.tika Tika]
           [org.apache.tika.sax BodyContentHandler]
           )
  (:use [clojure.java.io :only [input-stream]])
  )

;; TODO: add separate function to extract only meta-data

(def ^{:private true} tika-class (Tika.))

(defn conv-metadata [^Metadata mdata]
  (let [names (.names mdata)]
    (zipmap (map #(keyword (.toLowerCase %1)) names)
            (map #(seq (.getValues mdata %1)) names))))

(defprotocol TikaProtocol
  "Protocol for Tika library"
  (parse [this] "Performs parsing of given object")
  (detect-mime-type [this] "Detects mime-type of given object")
  )

(extend-protocol TikaProtocol
  InputStream
  (parse [^InputStream ifile]
         (let [parser (AutoDetectParser.)
               context (ParseContext.)
               metadata (Metadata.)
               handler (BodyContentHandler. -1)
               ]
           (.set context Parser parser)
           (.parse parser ifile handler metadata context)
           (assoc (conv-metadata metadata) :text (.toString handler))))
  (detect-mime-type [^InputStream ifile]
                    (.detect tika-class ifile)))

(extend-protocol TikaProtocol
  java.io.File
  (parse [^File file] (parse (input-stream file)))
  (detect-mime-type [^File file] (.detect tika-class file)))

(extend-protocol TikaProtocol
  String
  (parse [^String filename] (parse (input-stream filename)))
  (detect-mime-type [^String filename] (.detect tika-class filename)))

(extend-protocol TikaProtocol
  URL
  (parse [^URL url] (parse (input-stream url)))
  (detect-mime-type [^URL url] (.detect tika-class url)))

(defn detect-language
  "Detects language of given text"
  [^String text]
  (.getLanguage (LanguageIdentifier. text)))
