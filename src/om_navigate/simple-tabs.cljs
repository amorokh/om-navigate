(ns om-navigate.simple-tabs
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]
            [om-navigate.navigate :as nav]
            [om-navigate.sample-text]))

(def Ionicons (js/require "react-native-vector-icons/Ionicons"))
(def ionicons (partial e/create-element (.-default Ionicons)))

(def sample-text (om/factory om-navigate.sample-text/SampleText))

(defui MyNavScreen
  Object
  (render [this]
    (let [{:keys [banner]} (om/props this)]
      (e/scroll-view {:style {:marginTop 20}}
        (sample-text {:text banner})
        (e/button {:onPress #(nav/navigate-to this :Home)
                   :title "Go to home tab"})
        (e/button {:onPress #(nav/navigate-to this :Settings)
                   :title "Go to settings tab"})
        (e/button {:onPress #(nav/navigate-back this)
                   :title "Go back"})))))

(def my-nav-screen (om/factory MyNavScreen))

(defn load-icon [name params]
  (let [icon (str "ios-" name)
        tint (.-tintColor params)
        focused? (.-focused params)
        icon (if focused? icon (str icon "-outline"))]
    (ionicons {:name icon :size 26 :style {:color tint}})))

(defui MyHomeScreen
  static field navigationOptions
  #js {:tabBar #js {:label "Home" :icon #(load-icon "home" %)}}
  Object
  (render [this]
    (let [{:keys [navigation]} (om/props this)]
      (my-nav-screen {:banner "Home Tab" :navigation navigation}))))

(defui MyPeopleScreen
  static field navigationOptions
  #js {:tabBar #js {:label "People" :icon #(load-icon "people" %)}}
  Object
  (render [this]
    (let [{:keys [navigation]} (om/props this)]
      (my-nav-screen {:banner "People Tab" :navigation navigation}))))

(defui MyChatScreen
  static field navigationOptions
  #js {:tabBar #js {:label "Chat" :icon #(load-icon "chatboxes" %)}}
  Object
  (render [this]
    (let [{:keys [navigation]} (om/props this)]
      (my-nav-screen {:banner "Chat Tab" :navigation navigation}))))

(defui MySettingsScreen
  static field navigationOptions
  #js {:tabBar #js {:label "Settings" :icon #(load-icon "settings" %)}}
  Object
  (render [this]
    (let [{:keys [navigation]} (om/props this)]
      (my-nav-screen {:banner "Settings Tab" :navigation navigation}))))

(def routes
  {:Home {:screen MyHomeScreen :path ""}
   :People {:screen MyPeopleScreen :path "cart"}
   :Chat {:screen MyChatScreen :path "chat"}
   :Settings {:screen MySettingsScreen :path "settings"}})

(def SimpleTabs (nav/create-tab-navigator 
                  routes 
                  {:tabBarOptions {:activeTintColor "#e91e63"}}))

