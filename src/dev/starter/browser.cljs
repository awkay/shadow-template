(ns starter.browser
  (:require [boo.client :as client]
            [fulcro.client.logging :as log]))


(defn start []
  (log/set-level :all)
  (client/mount))

(defn ^:export init []
  (client/mount))

(client/mount)
