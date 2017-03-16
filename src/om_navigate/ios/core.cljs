(ns om-navigate.ios.core
  (:require [om.next :as om :refer-macros [defui]]
            [re-natal.support :as sup]
            [om-navigate.state :as state]
            [om-navigate.navigate :as nav]))

(set! js/window.React (js/require "react"))
(def ReactNative (js/require "react-native"))

(defn create-element [rn-comp opts & children]
  (apply js/React.createElement rn-comp (clj->js opts) children))

(def app-registry (.-AppRegistry ReactNative))
(def view (partial create-element (.-View ReactNative)))
(def text (partial create-element (.-Text ReactNative)))
(def image (partial create-element (.-Image ReactNative)))
(def button (partial create-element (.-Button ReactNative)))
(def touchable-highlight (partial create-element (.-TouchableHighlight ReactNative)))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defui RecentChatsScreen
  static field navigationOptions
  #js {:title "Recents"}

  static om/IQuery
  (query [this]
    '[:app/recent])
  
  Object
  (render [this]
    (view nil
      (text nil "List of recent chats...")
      (text nil (:app/recent (om/props this)))
      (button #js {:onPress #(nav/navigate-to this :chat)
                   :title "Chat with ...?"}))))

(defui AllContactsScreen
  static field navigationOptions
  #js {:title "Contacts"}

  static om/IQuery
  (query [this]
    '[:app/all])
  
  Object
  (render [this]
    (view nil
      (text nil "List of all contacts...")
      (text nil (:app/all (om/props this)))
      (button #js {:onPress #(nav/navigate-to this :chat)
                   :title "Chat with ...?"}))))

(def tab-routes {:recent {:screen RecentChatsScreen}
                 :contacts {:screen AllContactsScreen}})

(def TabNav (nav/tab-navigator tab-routes))

(defui HomeScreen
  static field navigationOptions
  #js {:title "Home"}

  static om/IQuery
  (query [this]
    '[:app/msg])
  
  Object
  (render [this]
    (let [{:keys [app/msg]} (om/props this)]
      (view {:style {:flexDirection "column" :margin 40 :alignItems "center"}}
        (text {:style {:fontSize 30 :fontWeight "100" :marginBottom 20 :textAlign "center"}} msg)
        (image {:source logo-img
                :style  {:width 80 :height 80 :marginBottom 30}})
        (touchable-highlight {:style {:backgroundColor "#999" :padding 10 :borderRadius 5}
                              :onPress #(nav/navigate-to this :chat {})}
          (text {:style {:color "white" :textAlign "center" :fontWeight "bold"}} "press me"))))))

(defui ChatScreen
  static field navigationOptions
  #js {:title "Chat"}
  
  static om/IQuery
  (query [this]
    '[:app/msg])
  
  Object
  (render [this]
    (let [{:keys [app/msg]} (om/props this)]
      (view {:style {:flexDirection "column" :margin 40 :alignItems "center"}}
        (text {:style {:fontSize 30 :fontWeight "100" :marginBottom 20 :textAlign "center"}} msg)
        (text nil "Chat with Hoyt!")))))

(def stack-routes {:home {:screen TabNav}
                   :chat {:screen ChatScreen}})

(def StackNav (nav/stack-navigator stack-routes))

(defonce RootNode (sup/root-node! 1))
(defonce app-root (om/factory RootNode))

(def AppRoot StackNav)

(defn init []
      (om/add-root! state/reconciler AppRoot 1)
      (.registerComponent app-registry "OmNavigate" (fn [] app-root)))
