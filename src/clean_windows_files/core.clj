(ns clean-windows-files.core
  (:gen-class)
  (:require [clojure.string :as st]
            [clojure.tools.cli :refer [cli]])
  (:import (java.io File)))

(defn clean-filename [^File f]
  (let [clean-fname (st/replace (.getName f) #"%20" "-")
        updated-path (str (.getParentFile f) "/" clean-fname)]
    (.renameTo f (File. updated-path))))

(defn clean-directory [^String dir]
  (->> (File. dir)
       file-seq
       (map clean-filename)
       dorun))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [[opts args banner] (cli args
                                ["-h" "--help" "Use -d or --directory to specify a directory to clean"
                                 :default false :flag true]
                                ["-d" "--directory" "Clean all filenames in directory"
                                 :flag false])]

    (when (:help opts)
      (println banner)
      (System/exit 0))
    (when (:directory opts)
      (clean-directory (:directory opts)))))
