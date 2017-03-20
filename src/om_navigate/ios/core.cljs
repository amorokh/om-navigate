(ns om-navigate.ios.core
  (:require [om.next :as om :refer-macros [defui]]
            [re-natal.support :as sup]
            [om-navigate.elements :as e]
            [om-navigate.state :as state]
            [om-navigate.navigate :as nav]
            [om-navigate.banner :as banner]
            [om-navigate.app :as app]))

(def logo-img (js/require "./images/cljs.png"))

(defui RecentChatsScreen
  static field navigationOptions
  #js {:title "Recents"}

  static om/IQuery
  (query [this]
    '[:app/recent])
  
  Object
  (render [this]
    (e/view nil
      (e/text nil "List of recent chats...")
      (e/text nil (:app/recent (om/props this)))
      (e/button #js {:onPress #(nav/navigate-to this :chat)
                     :title "Chat with ...?"}))))

(defui AllContactsScreen
  static field navigationOptions
  #js {:title "Contacts"}

  static om/IQuery
  (query [this]
    '[:app/all])
  
  Object
  (render [this]
    (e/view nil
      (e/text nil "List of all contacts...")
      (e/text nil (:app/all (om/props this)))
      (e/button #js {:onPress #(nav/navigate-to this :chat)
                     :title "Discuss with ...?"}))))

(def tab-routes {:recent {:screen RecentChatsScreen}
                 :contacts {:screen AllContactsScreen}})

(def TabNav (nav/create-tab-navigator tab-routes))

(defui HomeScreen
  static field navigationOptions
  #js {:title "Home"}

  static om/IQuery
  (query [this]
    '[:app/msg])
  
  Object
  (render [this]
    (let [{:keys [app/msg]} (om/props this)]
      (e/view {:style {:flexDirection "column" :margin 40 :alignItems "center"}}
        (e/text {:style {:fontSize 30 :fontWeight "100" :marginBottom 20 :textAlign "center"}} msg)
        (e/image {:source logo-img
                  :style  {:width 80 :height 80 :marginBottom 30}})
        (e/touchable-highlight {:style {:backgroundColor "#999" :padding 10 :borderRadius 5}
                                :onPress #(nav/navigate-to this :chat {})}
          (e/text {:style {:color "white" :textAlign "center" :fontWeight "bold"}} "press me"))))))

(defui ChatScreen
  static field navigationOptions
  #js {:title "Chat"}
  
  static om/IQuery
  (query [this]
    '[:app/msg])
  
  Object
  (render [this]
    (let [{:keys [app/msg]} (om/props this)]
      (e/view {:style {:flexDirection "column" :margin 40 :alignItems "center"}}
        (e/text {:style {:fontSize 30 :fontWeight "100" :marginBottom 20 :textAlign "center"}} msg)
        (e/text nil "Chat with Hoyt!")))))

(def stack-routes {:home {:screen TabNav}
                   :chat {:screen ChatScreen}})

(def StackNav (nav/create-stack-navigator stack-routes))

(defonce RootNode (sup/root-node! 1))
(defonce app-root (om/factory RootNode))

; (def AppRoot StackNav)
(def AppRoot app/AppNavigator)

(defn init []
      (om/add-root! state/reconciler AppRoot 1)
      (.registerComponent e/app-registry "OmNavigate" (fn [] app-root)))
