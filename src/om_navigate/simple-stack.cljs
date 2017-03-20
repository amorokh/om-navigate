(ns om-navigate.simple-stack
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]
            [om-navigate.navigate :as nav]
            [om-navigate.sample-text :as stext]))

(def sample-text (om/factory stext/SampleText))

(defui MyNavScreen
  Object
  (render [this]
    (let [{:keys [banner]} (om/props this)]
      (e/scroll-view nil
        (sample-text {:text banner})
        (e/button {:onPress #(nav/navigate-to this :Profile {:name "Jane"})
                   :title "Go to a profile screen"})
        (e/button {:onPress #(nav/navigate-to this :Photos {:name "Jane"})
                   :title "Go to a photos screen"})
        (e/button {:onPress #(nav/navigate-back this)
                   :title "Go back"})))))

(def my-nav-screen (om/factory MyNavScreen))

(defui MyHomeScreen
  static field navigationOptions #js {:title "Welcome"}
  Object
  (render [this]
    (let [{:keys [navigation]} (om/props this)]
      (my-nav-screen {:banner "Home Screen" :navigation navigation}))))
  
(defui MyPhotosScreen
  static field navigationOptions #js {:title "Photos"}
  Object
  (render [this]
    (let [{:keys [navigation]} (om/props this)
          name                 (.. navigation -state -params -name)]
      (my-nav-screen {:banner (str name "'s Photos") :navigation navigation}))))
  
(defui MyProfileScreen
  static field navigationOptions
  #js {:header (fn [navigation]
                 (let [name  (.. navigation -state -params -name)
                       mode  (.. navigation -state -params -mode)
                       edit? (= mode "edit")]
                   #js {:title (str name "'s Profile")
                        :right (e/button {:title (if edit? "Done" "Edit")
                                          :onPress #(.setParams navigation #js {:mode (if edit? "" "edit")})})}))}
  Object
  (render [this]
    (.log js/console "humdidum")
    (let [{:keys [navigation]} (om/props this)
          name (.. navigation -state -params -name)
          mode (.. navigation -state -params -mode)
          edit? (= mode "edit")
          banner (str name "'s Profile")
          banner (if edit? (str "Now editing " banner) banner)]
      (my-nav-screen {:banner banner :navigation navigation}))))

(def routes
  {:Home {:screen MyHomeScreen}
   :Profile {:screen MyProfileScreen :path "people/:name"}
   :Photos {:screen MyPhotosScreen :path "photos/:name"}})

(def SimpleStack (nav/create-stack-navigator routes))
