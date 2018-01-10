(ns boo.client-test-main
  (:require boo.tests-to-run
            [fulcro-spec.selectors :as sel]
            [fulcro-spec.suite :as suite]))

(enable-console-print!)

(suite/def-test-suite client-tests {:ns-regex #"boo..*-spec"}
  {:default   #{::sel/none :focused}
   :available #{:focused}})

