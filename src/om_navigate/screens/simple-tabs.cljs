(ns om-navigate.screens.simple-tabs
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]
            [om-navigate.navigate :as nav]
            [om-navigate.views.sample-text :refer [sample-text]]))

;; =============================================================================
;; Tab icon

(defn tabicon [name params]
  (let [icon (str "ios-" name)
        tint (.-tintColor params)
        focused? (.-focused params)
        icon (if focused? icon (str icon "-outline"))]
    (e/ionicons #js {:name icon :size 26 :style #js {:color tint}})))

;; =============================================================================
;; MyNavScreen

(defui MyNavScreen
  Object
  (render [this]
    (let [{:keys [banner]} (om/props this)]
      (e/scroll-view #js {:style #js {:marginTop 20}}
        (sample-text {:text banner})
        (e/button #js {:onPress #(nav/navigate-to this ::home)
                       :title "Go to home tab"})
        (e/button #js {:onPress #(nav/navigate-to this ::settings)
                       :title "Go to settings tab"})
        (e/button #js {:onPress #(nav/navigate-back this)
                       :title "Go back"})))))

(def my-nav-screen (om/factory MyNavScreen))

;; =============================================================================
;; MyHomeScreen

(defui MyHomeScreen
  static nav/INavOptions
  (options [this _ _ _]
    #js {:tabBarLabel "Home" 
         :tabBarIcon #(tabicon "home" %)})

  Object
  (render [this]
    (let [navigation (.. this -props -navigation)]
      (my-nav-screen {:banner "Home Tab" :navigation navigation}))))

;; =============================================================================
;; MyPeopleScreen

(defui MyPeopleScreen
  static nav/INavOptions
  (options [this _ _ _]
    #js {:tabBarLabel "People" 
         :tabBarIcon #(tabicon "people" %)})

  Object
  (render [this]
    (let [navigation (.. this -props -navigation)]
      (my-nav-screen {:banner "People Tab" :navigation navigation}))))

;; =============================================================================
;; MyChatScreen

(defui MyChatScreen
  static nav/INavOptions
  (options [this _ _ _]
    #js {:tabBarLabel "Chat" 
         :tabBarIcon #(tabicon "chatboxes" %)})

  Object
  (render [this]
    (let [navigation (.. this -props -navigation)]
      (my-nav-screen {:banner "Chat Tab" :navigation navigation}))))

;; =============================================================================
;; MySettingsScreen

(defui MySettingsScreen
  static nav/INavOptions
  (options [this _ _ _]
    #js {:tabBarLabel "Settings" 
         :tabBarIcon #(tabicon "settings" %)})

  Object
  (render [this]
    (let [navigation (.. this -props -navigation)]
      (my-nav-screen {:banner "Settings Tab" :navigation navigation}))))

;; =============================================================================
;; Routes

(def routes
  {::home #js {:screen MyHomeScreen 
               :path   ""}

   ::people #js {:screen MyPeopleScreen 
                 :path   "cart"}

   ::chat #js {:screen MyChatScreen
               :path   "chat"}
   
   ::settings #js {:screen MySettingsScreen 
                   :path   "settings"}})

;; =============================================================================
;; SimpleTabs

(def SimpleTabs (nav/tab-navigator 
                  routes 
                  #js {:tabBarOptions #js {:activeTintColor "#e91e63"}}))

