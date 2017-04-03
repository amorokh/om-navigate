(ns om-navigate.app
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]
            [om-navigate.navigate :as nav]
            [om-navigate.banner :refer [Banner]]
            [om-navigate.screens.simple-stack :refer [SimpleStack]]
            [om-navigate.screens.simple-tabs :refer [SimpleTabs]]
            [om-navigate.screens.drawer :refer [Drawer]]
            [om-navigate.screens.custom-tabs :refer [CustomTabs]]))

(def example-routes {:SimpleStack {:name        "Stack Example"
                                   :description "A card stack"
                                   :screen      SimpleStack}
                     :SimpleTabs  {:name        "Tabs Example"
                                   :description "Tabs following platform conventions"
                                   :screen      SimpleTabs}
                     :Drawer      {:name        "Drawer Example"
                                   :description "Android-style drawer navigation"
                                   :screen      Drawer}
                     :CustomTabs  {:name        "Custom Tabs"
                                   :description "Custom tabs with tab router"
                                   :screen      CustomTabs}})

(def styles {:item {:backgroundColor   "#fff"
                    :paddingHorizontal 16
                    :paddingVertical   12
                    :borderBottomWidth 1
                    :borderBottomColor "#ddd"}
             :image {:width        120
                     :height       120
                     :alignSelf    "center"
                     :marginBottom 20
                     :resizeMode   "contain"}
             :title {:fontSize   16
                     :fontWeight "bold"
                     :color      "#444"}
             :description {:fontSize 13
                           :color    "#999"}})

(def banner (om/factory Banner))

(defui MainScreen
  Object
  (render [this]
    (e/scroll-view nil
      (banner)
      (map
        (fn [[key {:keys [name description screen]}]]
          (e/touchable-opacity {:key key
                                :onPress #(nav/navigate-to this key)}
            (e/view {:style (:item styles)}
              (e/text {:style (:name styles)} name)
              (e/text {:style (:description styles)} description))))
        example-routes))))

(def AppNavigator (nav/create-stack-navigator 
                    (assoc example-routes :Index {:screen MainScreen})
                    {:initialRouteName "Index"
                     :headerMode "none"
                     :mode "modal"}))
