#_{:clj-kondo/ignore [:refer-all]}
(ns specter.core
  (:require [com.rpl.specter :refer :all]))

;; ;;;;;;;;;;;;;
;; Why Specter ?
;; ;;;;;;;;;;;;;

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Maintain your data structure's type
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; different inputs, same type of outputs (seq)
(map inc [1 2 3])    ; (2 3 4)
(map inc #{1 2 3 4}) ; (2 5 4 3)

;; produces vectors
(mapv inc [1 2 3])    ; [2 3 4]
(mapv inc #{1 2 3 4}) ; [2 5 4 3]

;; keeps input type

(transform ALL inc [1 2 3 4]) ; [2 3 4 5]

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Elegantly manipulate your nested data structures
;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; We want to inc all even values in a list of maps.

;; -------------
;; Clojure Style
;; -------------

(def data [{:a 1 :b 2} {:c 3} {:d 4}])

;; helper
(defn apply-fn-to-hashmap [f m]
  (into {} (for [[k v] m]
             [k (f v)])))

(apply-fn-to-hashmap inc {:a 1}) ; {:a 2}

(map (partial apply-fn-to-hashmap inc) data) ; ({:a 2, :b 3} {:c 4} {:d 5})

;; another helper
(defn inc-even [n]
  (if (even? n)
    (inc n)
    n))

(mapv (partial apply-fn-to-hashmap inc-even) data)  ; [{:a 1, :b 3} {:c 3} {:d 5}]

;; -------------
;; Specter Style
;; -------------

(transform [ALL MAP-VALS even?] inc data) ; [{:a 1, :b 3} {:c 3} {:d 5}]

;; ;;;;;;;;;;;;;;;;;;
;; How does it work ?
;; ;;;;;;;;;;;;;;;;;;

;;                similar to Specter's navigators
;;                       ↓↓↓↓↓↓↓↓↓↓
(get-in {:a {:b {:c 1}}} [:a :b :c]) ; 1
(get-in [:a {:b {:c 1}}] [1  :b :c]) ; 1

;; START
;; [{:a 1 :b 2} {:c 3} {:d 4}]

;; ALL
;; {:a 1 :b 2}
;; {:c 3}
;; {:d 4}

;; MAP-VALS
;; 1
;; 2
;; 3
;; 4

;; even?
;; 2
;; 4

;; <Navigation complete>

;; inc
;; 3
;; 5

;; <Reconstruct>

;; reverse even?
;; 1
;; 3
;; 3
;; 5

;; reverse MAP-VALS
;; {:a 1 :b 3}
;; {:c 3}
;; {:d 5}

;; reverse ALL
;; [{:a 1, :b 3} {:c 3} {:d 5}]

;; ;;;;;;;;;;;;;;;;;;;;;;;;;
;; What can you do with it ?
;; ;;;;;;;;;;;;;;;;;;;;;;;;;

;; Video 15:37
