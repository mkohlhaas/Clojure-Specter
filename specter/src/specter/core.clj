#_{:clj-kondo/ignore [:refer-all]}
(ns specter.core
  (:require
   [com.rpl.specter :refer :all]))

(declare MAP-VALS ALL filterer)

(transform [MAP-VALS MAP-VALS] inc {:a {:aa 1} :b {:ba -1 :bb 2}}) ; {:a {:aa 2}, :b {:ba 0, :bb 3}}

(select [MAP-VALS MAP-VALS] {:a {:aa 1} :b {:ba -1 :bb 2}}) ; [1 -1 2]
(select [MAP-VALS]          {:a {:aa 1} :b {:ba -1 :bb 2}}) ; [{:aa 1} {:ba -1, :bb 2}]

(select [ALL]          [{:a 1} {:a 2} {:a 4} {:a 3}]) ; [{:a 1} {:a 2} {:a 4} {:a 3}]
(select [ALL :a]       [{:a 1} {:a 2} {:a 4} {:a 3}]) ; [1 2 4 3]
(select [ALL :a even?] [{:a 1} {:a 2} {:a 4} {:a 3}]) ; [2 4]

(select [ALL]                      [[1 2 3 4] [] [5 3 2 18] [2 4 6] [12]]) ; [[1 2 3 4] [] [5 3 2 18] [2 4 6] [12]]
(select [ALL ALL]                  [[1 2 3 4] [] [5 3 2 18] [2 4 6] [12]]) ; [1 2 3 4 5 3 2 18 2 4 6 12]
(select [ALL ALL #(= 0 (mod % 3))] [[1 2 3 4] [] [5 3 2 18] [2 4 6] [12]]) ; [3 3 18 6 12]

(select [(filterer odd?) LAST] [2 1 3 6 9 4 8]) ; [9]
