(ns om-navigate.screens.simple-stack
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.state :as state]
            [om-navigate.elements :as e]
            [om-navigate.navigate :as nav]
            [om-navigate.views.sample-text :refer [sample-text]]))

;; =============================================================================
;; MyNavScreen

(defui MyNavScreen
  Object
  (render [this]
    (let [{:keys [banner]} (om/props this)]
      (e/scroll-view nil
        (sample-text {:text banner})
        (e/button #js {:onPress #(nav/navigate-to this :profile #js {:name "Jane"})
                       :title "Go to a profile screen"})
        (e/button #js {:onPress #(nav/navigate-to this :photos #js {:name "Jane"})
                       :title "Go to a photos screen"})
        (e/button #js {:onPress #(nav/navigate-back this)
                       :title "Go back"})
        (e/button #js {:onPress #(nav/reset this :home)
                       :title "Reset"})))))

(def my-nav-screen (om/factory MyNavScreen))

;; =============================================================================
;; MyHomeScreen

(defui MyHomeScreen
  static om/IQuery
  (query [this]
    [{:simple-stack/home [:title :header]}])

  static nav/INavOptions
  (options [this {:keys [simple-stack/home] :as props} _ _]
    #js {:headerTitle (:title home)})

  Object
  (render [this]
    (let [{:keys [simple-stack/home] :as props} (om/props this)]
      (my-nav-screen {:banner (:header home)}))))
  
;; =============================================================================
;; MyPhotosScreen

(defui MyPhotosScreen
  static nav/INavOptions
  (options [this _ _ _]
    #js {:headerTitle "Photos"})
  
  Object
  (render [this]
    (let [name (.. (nav/get-navigation this) -state -params -name)]
      (my-nav-screen {:banner (str name "'s Photos")}))))

;; =============================================================================
;; MyProfileScreen

(defui MyProfileScreen
  static om/IQuery
  (query [this]
    [{:simple-stack/profile [:name :editing?]}])

  static nav/INavOptions
  (options [this {:keys [simple-stack/profile]} navigation _]
    #js {:headerTitle (str (:name profile) "'s Profile")
         :headerRight (e/button #js {:title (if (:editing? profile) "Done" "Edit")
                                     :onPress (fn []
                                                (om/transact! state/reconciler '[(simple-stack/toggle-profile-editing)]))})})
  Object
  (render [this]
    (let [{:keys [simple-stack/profile] :as props} (om/props this)
          banner (str (:name profile) "'s Profile")
          banner (if (:editing? profile) (str "Now editing " banner) banner)]
      (my-nav-screen {:banner banner}))))

;; =============================================================================
;; Routes

(def routes 
  {::home #js {:screen MyHomeScreen}

   ::profile #js {:screen MyProfileScreen 
                  :path   "people/:name"}

   ::photos #js {:screen MyPhotosScreen
                 :path   "photos/:name"}})

;; =============================================================================
;; SimpleStack

(def SimpleStack (nav/stack-navigator routes))
