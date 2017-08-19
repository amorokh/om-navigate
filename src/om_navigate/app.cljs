(ns om-navigate.app
  (:require [om.next :as om :refer-macros [defui]]
            [cljs.pprint :as pp]
            [om-navigate.elements :as e]
            [om-navigate.navigate :as nav]
            [om-navigate.views.banner :refer [banner]]
            [om-navigate.screens.simple-stack :refer [SimpleStack]]
            [om-navigate.screens.simple-tabs :refer [SimpleTabs]]
            [om-navigate.screens.drawer :refer [Drawer]]
            [om-navigate.screens.custom-tabs :refer [CustomTabs]]))

;; =============================================================================
;; Styles

(def styles
  {:item #js {:backgroundColor   "#fff"
              :paddingHorizontal 16
              :paddingVertical   12
              :borderBottomWidth 1
              :borderBottomColor "#ddd"}
  
   :image #js {:width        120
               :height       120
               :alignSelf    "center"
               :marginBottom 20
               :resizeMode   "contain"}
   
   :title #js {:fontSize   16
               :fontWeight "bold"
               :color      "#444"}
   
   :description #js {:fontSize 13
                     :color    "#999"}})

;; =============================================================================
;; Routes

(def routes 
  {::simple-stack #js {:name        "Stack Example"
                       :description "A card stack"
                       :screen      SimpleStack}
   
   ::simple-tabs #js {:name        "Tabs Example"
                      :description "Tabs following platform conventions"
                      :screen      SimpleTabs}
   
   ::drawer #js {:name        "Drawer Example"
                 :description "Android-style drawer navigation"
                 :screen      Drawer}

   ::custom-tabs #js {:name        "Custom Tabs"
                      :description "Custom tabs with tab router"
                      :screen      CustomTabs}})

;; =============================================================================
;; MainScreen

(defui MainScreen
  Object
  (render [this]
    (e/scroll-view nil
      (banner)
      (map
        (fn [[k v]]
          (e/touchable-opacity #js {:key k
                                    :onPress #(nav/navigate-to this k)}
            (e/view #js {:style (:item styles)}
              (e/text #js {:style (:name styles)} (.-name v))
              (e/text #js {:style (:description styles)} (.-description v)))))
        routes))))

;; =============================================================================
;; AppNavigator

(def AppNavigator (nav/stack-navigator 
                    (assoc routes ::index #js {:screen MainScreen})
                    #js {:initialRouteName "index"
                         :headerMode       "none"
                         :mode             "modal"}))
