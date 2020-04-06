(ns markdownify.main
  (:require [reagent.core :as reagent]
            [reagent.dom :as dom]))


(defn app []
  [:h1 "Hi, there!"])


;;
;; -----------
;;

(defn mount! []
  (dom/render
   [app]
   (.getElementById js/document "app")))

(defn main! []
  (mount!))

(defn reload! []
  (mount!))
