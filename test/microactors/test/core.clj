(ns microactors.test.core
  (:use [microactors.core])
  (:use [clojure.test]))

(defbeh counterbeh [cont cur]
    ([:inc] (if (>= (inc cur) 3) 
             (post-msg cont cur)
             (become (counterbeh cont (inc cur))))))

(deftest replace-me 
  (let [prom (promise)
        actor (new-actor (counterbeh prom 0))]
       (post-msg actor [:inc])
       (post-msg actor [:inc])
       (post-msg actor [:inc])
       (is @prom 3)))       
  

(deftest gather-tests-with-reset-true
  (let [prom (promise)
        gth (new-actor (gather :idx #(= (count %) 3) prom true))]
       (post-msg gth [{:idx 1}])
       (post-msg gth [{:idx 2}])
       (post-msg gth [{:idx 3}])
       (is @prom {1 {:idx 1}
                  2 {:idx 2}
                  3 {:idx 3}})))

(deftest gather-tests-with-reset-false
  (let [prom (promise)
        gth (new-actor (gather :idx #(= (count %) 3) prom false))]
       (post-msg gth [{:idx 1}])
       (post-msg gth [{:idx 2}])
       (post-msg gth [{:idx 3}])
       (is @prom {1 {:idx 1}
                  2 {:idx 2}
                  3 {:idx 3}})))
       
