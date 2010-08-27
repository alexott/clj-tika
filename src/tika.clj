;; Clojure Interface to Apache Tika library
;; TODO: add functions for java.net.URL class
(ns tika
  (:import (java.io InputStream File FileInputStream))
  (:import (java.net URL))
  (:import (org.apache.tika.parser Parser AutoDetectParser ParseContext))
  (:import (org.apache.tika.language LanguageIdentifier))
  (:import (org.apache.tika.metadata Metadata))
  (:import (org.apache.tika Tika))
  (:import (org.apache.tika.sax BodyContentHandler))
  )

(def #^{:private true} tika-class (Tika.))

(defn- conv-metadata [#^Metadata mdata]
  (let [names (.names mdata)]
    (zipmap (map #(keyword (.toLowerCase %1)) names)
            (map #(seq (.getValues mdata %1)) names))))

(defprotocol TikaProtocol
  "Protocol for Tika"
  (parse [this] "Perform parsing of given object")
  (detect-mime-type [this] "Detect mime type of object")
  )

(extend-protocol TikaProtocol
  InputStream
  (parse [ifile]
         (let [parser (new AutoDetectParser)
               context (new ParseContext)
               metadata (new Metadata)
               handler (new BodyContentHandler)
               ]
           (.set context Parser parser)
           (.parse parser ifile handler metadata context)
           (assoc (conv-metadata metadata) :text (.toString handler))))
  (detect-mime-type [ifile]
                    (.detect tika-class ifile)))

(extend-protocol TikaProtocol
  File
  (parse [file]
         (parse (new FileInputStream file)))
  (detect-mime-type [file]
                    (.detect tika-class file)))

(extend-protocol TikaProtocol
  String
  (parse [filename]
         (parse (FileInputStream. (File. filename))))
  (detect-mime-type [filename]
                    (.detect tika-class filename)))

(extend-protocol TikaProtocol
  URL
  (parse [url] (parse (.openStream url)))
  (detect-mime-type [url]
                    (.detect tika-class url)))

(defn detect-language
  "Detects language of given text"
  [#^String text]
  (.getLanguage (LanguageIdentifier. text)))
