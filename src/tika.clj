;; Clojure Interface to Apache Tika library

(ns tika
  (:import (java.io InputStream File FileInputStream))
  (:import (org.apache.tika.parser Parser AutoDetectParser ParseContext))
  (:import (org.apache.tika.metadata Metadata))
  (:import (org.apache.tika.sax BodyContentHandler))
  )

(defn conv-metadata [#^Metadata mdata]
  (let [names (.names mdata)]
    (zipmap (map #(keyword (.toLowerCase %1)) names)
            (map #(seq (.getValues mdata %1)) names))))


(defn parse-stream
  "Parses Tika-supported stream"
  [#^InputStream ifile]
  (let [parser (new AutoDetectParser)
        context (new ParseContext)
        metadata (new Metadata)
        handler (new BodyContentHandler)
        ]
    (.set context Parser parser)
    (.parse parser ifile handler metadata context)
    (.close ifile)
    (let [mdata (conv-metadata metadata)
          txt (.toString handler)]
      {:metadata mdata :text txt})))

(defn parse-file
  "Parses Tika-supported file"
  [#^File file]
  (parse-stream (new FileInputStream file)))
