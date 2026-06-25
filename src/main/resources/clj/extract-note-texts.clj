;
; Copyright 2026 Pravles Redneckoff
;
; Permission is hereby granted, free of charge, to any person obtaining a copy
; of
; this software and associated documentation files (the “Software”), to deal in
; the Software without restriction, including without limitation the rights to
; use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
; of
; the Software, and to permit persons to whom the Software is furnished to do
; so,
; subject to the following conditions:
;
; The above copyright notice and this permission notice shall be included in
; all
; copies or substantial portions of the Software.
;
; THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
; IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
; FITNESS
; FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
; OR
; COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
; WHETHER
; IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
; CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
;

(ns extract-note-texts)

(require '[clojure.string :as str]
         '[clojure.java.io :as io]
         '[clojure.pprint :as pprint])
(import 'org.apache.commons.lang3.StringUtils)
(import 'us.bpsm.edn.Keyword)

(def nl (System/getProperty "line.separator"))

(def note-header-re
  #"^\*\*\s+[A-Za-z0-9.]+\s+\(\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2}\)(?::.*)?$")

(defn note-header?
  [line]
  (boolean (re-matches note-header-re line)))

(defn extract-note-txts
  [zk-lines]
  (let [lines (->> (str/split-lines zk-lines)
                   (remove #(str/starts-with? % "#")))]
    (->> lines
         (reduce
           (fn [{:keys [notes current]} line]
             (cond
               (note-header? line)
               {:notes   (if (seq current)
                           (conj notes (str/join nl current))
                           notes)
                :current [line]}

               current
               {:notes notes
                :current (conj current line)}

               :else
               {:notes notes
                :current nil}))
           {:notes [] :current nil})
         ((fn [{:keys [notes current]}]
            (if (seq current)
              (conj notes (str/join nl current))
              notes))))))

(defn гав
  [ctx]
  (let [zk-lines  (get ctx "zk-lines")
        note-txts (extract-note-txts zk-lines)]
    (println "extract-note-texts")
    (println "note count:" (count note-txts))
    (.put ctx "note-txts" note-txts)
    ctx))
