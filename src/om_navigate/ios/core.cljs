(ns om-navigate.ios.core
  (:require [om.next :as om :refer-macros [defui]]
            [re-natal.support :as sup]
            [om-navigate.elements :as e]
            [om-navigate.state :as state]
            [om-navigate.app :as app]))

(defonce RootNode (sup/root-node! 1))
(defonce app-root (om/factory RootNode))

(def AppRoot app/AppNavigator)

(defn init []
      (om/add-root! state/reconciler AppRoot 1)
      (.registerComponent e/app-registry "OmNavigate" (fn [] app-root)))
