(ns clj-noobie.tests.logic)


; Problem: department can not exists
(defn queue-has-space?
  [hospital department]
  (-> hospital
      department
      count
      (< 5)))

; using if-let to thread not existent department
(defn queue-has-space?
  [hospital department]
  (if-let [queue (get hospital department)]
    (< (count queue) 5)))

; Other way to solve the case where the department do not exist
; some-> stop the execution when some command return nil
(defn queue-has-space?
  [hospital department]
  (some-> hospital
      department
      count
      (< 5)))

(defn check-in
  [hospital department person]
  (if (get hospital department)
    (if (queue-has-space? hospital department)
      (update hospital department conj person)
      (throw (ex-info "Department has not space!!" {:person person :department department})))
    hospital))



