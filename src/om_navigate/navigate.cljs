(ns om-navigate.navigate
  (:require [om.next :as om :refer-macros [ui]]))

(def ReactNavigation (js/require "react-navigation"))
(def StackNavigator (.-StackNavigator ReactNavigation))
(def TabNavigator (.-TabNavigator ReactNavigation))
(def DrawerNavigator (.-DrawerNavigator ReactNavigation))

(defn navigate-to 
  ([c target] (navigate-to c target {}))
  ([c target params]
   (let [props (om/props c)
         navigation (:navigation props)]
     (.navigate navigation (name target) (clj->js params)))))

(defn- navigator-factory
  [navigator navigation props]
  (js/React.createElement navigator
    #js {:navigation navigation
         :screenProps props}))

(defn- create-screen-proxy [screen]
  (om/ui
    static field navigationOptions (.-navigationOptions screen)
    Object
    (render [this]
      (let [screen-factory (om/factory screen)
            navigation     (.. this -props -navigation)
            screen-props   (.. this -props -screenProps)
            props          (assoc screen-props :navigation navigation)]
        (screen-factory props)))))

(defn- create-navigator-proxy [navigator]
  (om/ui
    static field navigator-proxy? true
    static field router (.-router navigator)
    Object
    (shouldComponentUpdate [this _ _] true)
    (render [this]
      (let [navigation   (.. this -props -navigation)
            screen-props (.. this -props -screenProps)
            om-props     (om/props this)
            props        (if om-props om-props screen-props)]
        (navigator-factory navigator navigation props)))))

(defn- transform-routes
  [routes]
  (reduce-kv
    (fn [acc k {:keys [screen] :as v}]
      (assoc acc k
        (if (.-navigator-proxy? screen) 
          v 
          (assoc v :screen (create-screen-proxy screen)))))
    {}
    routes))

(defn- extract-queries
  [routes]
  (let [screens (map (fn [[_ route]] (:screen route)) routes)]
    (mapcat om/get-query screens)))

(defn- create-navigator
  [routes factory]
  (let [queries   (into [] (extract-queries routes))
        navigator (factory (clj->js (transform-routes routes)))
        proxy     (create-navigator-proxy navigator)]
    (when (seq queries)
      (specify! proxy om/IQuery (query [this] queries)))
    proxy))

(defn create-stack-navigator
  [routes]
  (create-navigator routes #(StackNavigator %)))

(defn create-tab-navigator
  [routes]
  (create-navigator routes #(TabNavigator %)))

(defn create-drawer-navigator
  [routes]
  (create-navigator routes #(DrawerNavigator %)))
