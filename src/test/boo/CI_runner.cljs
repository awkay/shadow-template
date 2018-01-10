(ns boo.CI-runner
  (:require                                                 ;[jx.reporter.karma :refer-macros [run-tests run-all-tests]]
    [cljs.test :refer [run-all-tests]]
    boo.tests-to-run))

(enable-console-print!)

; runs all tests in all namespaces
(defn ^:export run []
  (run-all-tests #"^boo.*-spec$"))


