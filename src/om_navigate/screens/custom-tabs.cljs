(ns om-navigate.screens.custom-tabs
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]
            [om-navigate.navigate :as nav]
            [om-navigate.screens.sample-text :refer [SampleText]]))

(def styles {:container {:marginTop 20}
             :tab-container {:flexDirection "row"
                             :height 40}
             :tab {:flex 1
                   :alignItems "center"
                   :justifyContent "center"
                   :margin 4
                   :borderWidth 1
                   :borderColor "#ddd"
                   :borderRadius 4}})

(def sample-text (om/factory SampleText))

(defui MyNavScreen
  Object
  (render [this]
    (let [{:keys [banner]} (om/props this)]
      (e/scroll-view {:style {:marginTop 20}}
        (sample-text {:text banner})
        (e/button {:onPress #(nav/navigate-back this)
                   :title "Go back"})))))

(def my-nav-screen (om/factory MyNavScreen))

(defui MyHomeScreen
  Object
  (render [this]
    (let [navigation (.. this -props -navigation)]
      (my-nav-screen {:banner "Home Screen" :navigation navigation}))))
  
(defui MyNotificationsScreen
  Object
  (render [this]
    (let [navigation (.. this -props -navigation)]
      (my-nav-screen {:banner "Notifications Screen" :navigation navigation}))))
  
(defui MySettingsScreen
  Object
  (render [this]
    (let [navigation (.. this -props -navigation)]
      (my-nav-screen {:banner "Settings Screen" :navigation navigation}))))
  
(defui CustomTabBar
  Object
  (render [this]
    (.log js/console this)
    (let [{:keys [navigation]} (om/props this)
          routes (.. navigation -state -routes)]
      (e/view {:style (:tab-container styles)}
        (map (fn [route]
               (let [name (.-routeName route)]
                 (e/touchable-opacity {:onPress #(nav/navigate-to this name)
                                       :style (:tab styles)
                                       :key name}
                   (e/text nil name))))
             routes)))))

(def custom-tab-bar (om/factory CustomTabBar))

(defui CustomTabView
  Object
  (shouldComponentUpdate [this _ _] true)
  (render [this]
    (let [props        (.-props this)
          screen-props (.-screenProps props)
          navigation   (.-navigation props)
          state        (.-state navigation)
          routes       (.-routes state)
          index        (.-index state)
          router       (.-router props)
          active       (.getComponentForState router state)]
      (e/view {:style (:container styles)}
        (custom-tab-bar {:navigation navigation})
        (js/React.createElement 
          active 
          (nav/add-navigation-helpers #js {:navigation navigation :state (get routes index) :screenProps screen-props}))))))

(def routes {:Home 
               {:screen MyHomeScreen
                :path   ""}
             :Notifications 
               {:screen MyNotificationsScreen
                :path "notifications"}
             :Settings 
               {:screen MySettingsScreen
                :path "settings"}})

(def CustomTabs (nav/create-custom-navigator
                  CustomTabView 
                  nav/create-tab-router
                  routes
                  {:initialRouteName "Home"}))
