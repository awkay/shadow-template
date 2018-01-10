(ns starter.browser
  (:require [boo.client :as client]))

(defn start []
  (js/console.log :start)
  (client/mount))

(defn ^:export init []
  (js/console.log :init)
  (client/mount))

(client/mount)
