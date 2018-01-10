(defproject boo "0.1.0-SNAPSHOT"
  :description "My Cool Project"
  :license {:name "MIT" :url "https://opensource.org/licenses/MIT"}
  :min-lein-version "2.7.0"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [thheller/shadow-cljs "2.0.128"]
                 [fulcrologic/fulcro "2.1.0"]

                 ;[karma-reporter "3.0.0-alpha1" :scope "test"]
                 [fulcrologic/fulcro-spec "2.0.0-beta3" :scope "test" :exclusions [fulcrologic/fulcro]]]

  :uberjar-name "boo.jar"

  :source-paths ["src/dev" "src/main" "src/cards" "src/test"]
  :test-paths ["src/test"]

  :clean-targets ^{:protect false} ["target" "resources/public/js" "resources/private"]

  :aliases {"dev" ["run" "-m" "shadow.cljs.devtools.cli" "watch" "app" "test" "cards"]
            "ci"  ["run" "-m" "shadow.cljs.devtools.cli" "compile" "ci-tests"]}

  :test-refresh {:report       fulcro-spec.reporters.terminal/fulcro-report
                 :with-repl    true
                 :changes-only true}


  :cljsbuild {:builds [{:id           "production"
                        :source-paths ["src/main"]
                        :jar          true
                        :compiler     {:asset-path    "js/prod"
                                       :main          boo.client-main
                                       :optimizations :advanced
                                       :source-map    "resources/public/js/boo.js.map"
                                       :output-dir    "resources/public/js/prod"
                                       :output-to     "resources/public/js/boo.js"}}]}

  :profiles {:uberjar    {:main           boo.server-main
                          :aot            :all
                          :jar-exclusions [#"public/js/prod" #"com/google.*js$"]
                          :prep-tasks     ["clean" ["clean"]
                                           "compile" ["with-profile" "production" "cljsbuild" "once" "production"]]}
             :production {}
             :dev        {:source-paths ["src/dev" "src/main" "src/test" "src/cards"]

                          :jvm-opts     ["-XX:-OmitStackTraceInFastThrow" "-client" "-XX:+TieredCompilation" "-XX:TieredStopAtLevel=1"
                                         "-Xmx1g" "-XX:+UseConcMarkSweepGC" "-XX:+CMSClassUnloadingEnabled" "-Xverify:none"]


                          :figwheel     {:css-dirs ["resources/public/css"]}

                          :cljsbuild    {:builds
                                         [{:id           "dev"
                                           :figwheel     {:on-jsload "cljs.user/mount"}
                                           :source-paths ["src/dev" "src/main"]
                                           :compiler     {:asset-path           "js/dev"
                                                          :main                 cljs.user
                                                          :optimizations        :none
                                                          :output-dir           "resources/public/js/dev"
                                                          :output-to            "resources/public/js/boo.js"
                                                          :preloads             [devtools.preload fulcro.inspect.preload]
                                                          :source-map-timestamp true}}
                                          {:id           "i18n" ;for gettext string extraction
                                           :source-paths ["src/main"]
                                           :compiler     {:asset-path    "i18n"
                                                          :main          boo.client-main
                                                          :optimizations :whitespace
                                                          :output-dir    "i18n/tmp"
                                                          :output-to     "i18n/i18n.js"}}
                                          {:id           "test"
                                           :source-paths ["src/test" "src/main"]
                                           :figwheel     {:on-jsload "boo.client-test-main/client-tests"}
                                           :compiler     {:asset-path    "js/test"
                                                          :main          boo.client-test-main
                                                          :optimizations :none
                                                          :output-dir    "resources/public/js/test"
                                                          :output-to     "resources/public/js/test/test.js"
                                                          :preloads      [devtools.preload]}}
                                          {:id           "automated-tests"
                                           :source-paths ["src/test" "src/main"]
                                           :compiler     {:asset-path    "js/ci"
                                                          :main          boo.CI-runner
                                                          :optimizations :none
                                                          :output-dir    "resources/private/js/ci"
                                                          :output-to     "resources/private/js/unit-tests.js"}}
                                          {:id           "cards"
                                           :figwheel     {:devcards true}
                                           :source-paths ["src/main" "src/cards"]
                                           :compiler     {:asset-path           "js/cards"
                                                          :main                 boo.cards
                                                          :optimizations        :none
                                                          :output-dir           "resources/public/js/cards"
                                                          :output-to            "resources/public/js/cards.js"
                                                          :preloads             [devtools.preload]
                                                          :source-map-timestamp true}}]}

                          :plugins      [[com.jakemccrary/lein-test-refresh "0.17.0"]]

                          :dependencies [[binaryage/devtools "0.9.7"]
                                         [fulcrologic/fulcro-inspect "2.0.0-alpha1"]
                                         [org.clojure/tools.namespace "0.3.0-alpha4"]
                                         [org.clojure/tools.nrepl "0.2.13"]
                                         [com.cemerick/piggieback "0.2.2"]
                                         [devcards "0.2.4" :exclusions [cljsjs/react cljsjs/react-dom]]]
                          :repl-options {:init-ns          user
                                         :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})
