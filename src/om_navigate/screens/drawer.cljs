(ns om-navigate.screens.drawer
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]
            [om-navigate.navigate :as nav]
            [om-navigate.views.sample-text :refer [sample-text]]))

;; =============================================================================
;; Drawer icon

(defn drawer-icon [name params]
  (let [tint (.-tintColor params)]
    (e/material-icons #js {:name name :size 24 :style #js {:color tint}})))

;; =============================================================================
;; MyNavScreen

(defui MyNavScreen
  Object
  (render [this]
    (let [{:keys [banner]} (om/props this)]
      (e/scroll-view #js {:style #js {:marginTop 20}}
        (sample-text {:text banner})
        (e/button #js {:onPress #(nav/open-drawer this)
                       :title "Open drawer"})
        (e/button #js {:onPress #(nav/navigate-back this)
                       :title "Go back"})))))

(def my-nav-screen (om/factory MyNavScreen))

;; =============================================================================
;; InboxScreen

(defui InboxScreen
  static nav/INavOptions
  (options [this _ _ _]
    #js {:drawerLabel "Inbox" 
         :drawerIcon #(drawer-icon "move-to-inbox" %)})

  Object
  (render [this]
    (my-nav-screen {:banner "Inbox Screen"})))

;; =============================================================================
;; DraftsScreen

(defui DraftsScreen
  static nav/INavOptions
  (options [this _ _ _]
    #js {:drawerLabel "Drafts" 
         :drawerIcon #(drawer-icon "drafts" %)})

  Object
  (render [this]
    (my-nav-screen {:banner "Drafts Screen"})))

;; =============================================================================
;; Routes

(def routes 
  {::inbox #js {:screen InboxScreen
                :path   "/"}
   
   ::drafts #js {:screen DraftsScreen
                 :path   "/sent"}})

;; =============================================================================
;; Drawer

(def Drawer (nav/drawer-navigator
              routes
              #js {:initialRouteName "drafts"
                   :contentOptions #js {:activeTintColor "#e91e63"}}))
