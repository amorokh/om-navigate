(ns om-navigate.screens.drawer
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]
            [om-navigate.navigate :as nav]
            [om-navigate.screens.sample-text]))

(def MaterialIcons (js/require "react-native-vector-icons/MaterialIcons"))
(def material-icons (partial e/create-element (.-default MaterialIcons)))

(defn load-icon [name params]
  (let [tint (.-tintColor params)]
    (material-icons {:name name :size 24 :style {:color tint}})))

(def sample-text (om/factory om-navigate.screens.sample-text/SampleText))

(defui MyNavScreen
  Object
  (render [this]
    (let [{:keys [banner]} (om/props this)]
      (e/scroll-view {:style {:marginTop 20}}
        (sample-text {:text banner})
        (e/button {:onPress #(nav/navigate-to this :DrawerOpen)
                   :title "Open drawer"})
        (e/button {:onPress #(nav/navigate-back this)
                   :title "Go back"})))))

(def my-nav-screen (om/factory MyNavScreen))

(defui InboxScreen
  static field navigationOptions
  #js {:drawer #js {:label "Inbox" :icon #(load-icon "move-to-inbox" %)}}
  Object
  (render [this]
    (let [{:keys [navigation]} (om/props this)]
      (my-nav-screen {:banner "Inbox Screen" :navigation navigation}))))

(defui DraftsScreen
  static field navigationOptions
  #js {:drawer #js {:label "Drafts" :icon #(load-icon "drafts" %)}}
  Object
  (render [this]
    (let [{:keys [navigation]} (om/props this)]
      (my-nav-screen {:banner "Drafts Screen" :navigation navigation}))))

(def routes {:Inbox  {:screen InboxScreen
                      :path   "/"}
             :Drafts {:screen DraftsScreen
                      :path   "/sent"}})

(def Drawer (nav/create-drawer-navigator
              routes
              {:initialRouteName "Drafts"
               :contentOptions {:activeTintColor "#e91e63"}}))
