(defproject clj-noobie "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/" }
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.datomic/datomic-pro "1.0.6344"]
                 [prismatic/schema "1.1.12"]
                 [org.clojure/test.check "0.10.0"]]
  :repl-options {:init-ns clj-noobie.core})
