(ns markdownify.main
  (:require [reagent.core :as reagent]
            [reagent.dom :as dom]
            ["showdown" :as showdown]))


(defonce markdown (reagent/atom ""))

(defonce showdown-converter (showdown/Converter.))

(defn md->html [md]
  (.makeHtml showdown-converter md))

(defn copy-to-clipboard [s]
  (let [el (.createElement js/document "textarea")
        selected (when (pos? (-> js/document .getSelection .-rangeCount))
                   (-> js/document .getSelection (.getRangeAt 0)))]
    (set! (.-value el) s)
    (.setAttribute el "readonly" "")
    (set! (-> el .-style .-position) "absolute")
    (set! (-> el .-style .-left) "-9999px")
    (-> js/document .-body (.appendChild el))
    (.select el)
    (.execCommand js/document "copy")
    (-> js/document .-body (.removeChild el))
    (when selected
      (-> js/document .getSelection .removeAllRanges)
      (-> js/document .getSelection (.addRange selected)))))


(defn app []
  [:div
   [:h1 "Hi, there!"]
   [:div
    {:style {:display :flex}}
    [:div
     {:style {:flex "1"}}
     [:h2 "Markdown"]
     [:textarea
      {:on-change #(reset! markdown (-> % .-target .-value))
       :value @markdown
       :style {:resize "none"
               :height "350px"
               :width "100%"}}]
     [:button
      {:on-click #(copy-to-clipboard @markdown)
       :style {:background-color :green
               :padding "1em"
               :color :white
               :border-radius 10}}
      "Copy Markdown"]]

    [:div
     {:style {:flex "1"
              :padding-left "2em"}}
     [:h2 "HTML Preview"]
     [:div {:style {:height "350px"}
            :dangerouslySetInnerHTML {:__html (md->html @markdown)}}]
     [:button
      {:on-click #(copy-to-clipboard (md->html @markdown))
       :style {:background-color :green
               :padding "1em"
               :color :white
               :border-radius 10}}
      "Copy HTML"]]]])


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
