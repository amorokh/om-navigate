(ns om-navigate.screens.custom-tabs
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]
            [om-navigate.navigate :as nav]
            [om-navigate.views.sample-text :refer [sample-text]]))

;; =============================================================================
;; Styles

(def styles 
  {:container #js {:marginTop 20}

   :tab-container #js {:flexDirection "row"
                       :height        40}

   :tab #js {:flex           1
             :alignItems     "center"
             :justifyContent "center"
             :margin         4
             :borderWidth    1
             :borderColor    "#ddd"
             :borderRadius   4}})

;; =============================================================================
;; MyNavScreen

(defui MyNavScreen
  Object
  (render [this]
    (let [{:keys [banner]} (om/props this)]
      (e/scroll-view #js {:style #js {:marginTop 20}}
        (sample-text {:text banner})
        (e/button #js {:onPress #(nav/navigate-back this)
                       :title "Go back"})))))

(def my-nav-screen (om/factory MyNavScreen))

;; =============================================================================
;; MyHomeScreen

(defui MyHomeScreen
  static nav/INavOptions
  (options [this _ _ _]
    #js {:title "Home"})

  Object
  (render [this]
    (my-nav-screen {:banner "Home Screen"})))
  
;; =============================================================================
;; MyNotificationsScreen

(defui MyNotificationsScreen
  static nav/INavOptions
  (options [this _ _ _]
    #js {:title "Notifications"})

  Object
  (render [this]
    (my-nav-screen {:banner "Notifications Screen"})))
  
;; =============================================================================
;; MySettingsScreen

(defui MySettingsScreen
  static nav/INavOptions
  (options [this _ _ _]
    #js {:title "Settings"})

  Object
  (render [this]
    (my-nav-screen {:banner "Settings Screen"})))

;; =============================================================================
;; CustomTabBar

(defui CustomTabBar
  Object
  (render [this]
    (let [{:keys [screen-props router]} (om/props this)
          navigation (nav/get-navigation this)
          routes     (.. navigation -state -routes)]
      (e/view #js {:style (:tab-container styles)}
        (map 
          (fn [route]
            (let [name (.-routeName route)
                  snav (nav/add-navigation-helpers #js {:dispatch #(.dispatch navigation) :state route})
                  options (.getScreenOptions router snav screen-props)
                  title (or (.-title options) name)]
              (e/touchable-opacity #js {:onPress #(nav/navigate-to this name)
                                        :style (:tab styles)
                                        :key name}
                (e/text nil title))))
          routes)))))

(def custom-tab-bar (om/factory CustomTabBar))

;; =============================================================================
;; CustomTabView

(defui CustomTabView
  Object
  (shouldComponentUpdate [this _ _] true)
  (render [this]
    (let [navigation   (nav/get-navigation this)
          routes       (.. navigation -state -routes)
          index        (.. navigation -state -index)
          screen-props (.. this -props -screenProps)
          router       (.. this -props -router)
          active       (.getComponentForState router (.-state navigation))]
      (e/view #js {:style (:container styles)}
        (custom-tab-bar {:screen-props screen-props :router router})
        (js/React.createElement 
          active 
          (nav/add-navigation-helpers #js {:navigation navigation :state (get routes index) :screenProps screen-props}))))))

;; =============================================================================
;; Routes

(def routes 
  {::home #js {:screen MyHomeScreen
               :path   ""}

   ::notifications #js {:screen MyNotificationsScreen
                        :path   "notifications"}

   ::settings #js {:screen MySettingsScreen
                   :path   "settings"}})

;; =============================================================================
;; CustomTabs

(def CustomTabs (nav/custom-navigator
                  CustomTabView 
                  nav/tab-router
                  routes
                  #js {:initialRouteName "home"}))
