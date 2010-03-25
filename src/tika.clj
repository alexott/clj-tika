;; Clojure Interface to Apache Tika library
;; TODO: add functions for java.net.URL class
(ns tika
  (:import (java.io InputStream File FileInputStream))
  (:import (java.net URL))
  (:import (org.apache.tika.parser Parser AutoDetectParser ParseContext))
  (:import (org.apache.tika.metadata Metadata))
  (:import (org.apache.tika Tika))
  (:import (org.apache.tika.sax BodyContentHandler))
  )

(def #^{:private true} tika-class (Tika.))

(defn- conv-metadata [#^Metadata mdata]
  (let [names (.names mdata)]
    (zipmap (map #(keyword (.toLowerCase %1)) names)
            (map #(seq (.getValues mdata %1)) names))))

(defmulti parse class)

(defmethod parse InputStream [ifile]
  (let [parser (new AutoDetectParser)
        context (new ParseContext)
        metadata (new Metadata)
        handler (new BodyContentHandler)
        ]
    (.set context Parser parser)
    (.parse parser ifile handler metadata context)
    (assoc (conv-metadata metadata) :text (.toString handler))))

(defmethod parse String [filename]
  (parse (FileInputStream. (File. filename))))

(defmethod parse File [file]
  (parse (new FileInputStream file)))

;;
(defmulti detect-mime-type class)
(defmethod detect-mime-type InputStream [ifile]
  (.detect tika-class ifile))

(defmethod detect-mime-type String [filename]
  (.detect tika-class filename))

(defmethod detect-mime-type File [file]
  (.detect tika-class file))

