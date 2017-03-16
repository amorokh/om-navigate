(ns om-navigate.navigate
  (:require [om.next :as om :refer-macros [ui]]))

(def ReactNavigation (js/require "react-navigation"))
(def StackNavigator (.-StackNavigator ReactNavigation))
(def TabNavigator (.-TabNavigator ReactNavigation))

(defn navigate-to 
  ([c target] (navigate-to c target {}))
  ([c target params]
   (let [props (om/props c)
         navigation (:navigation props)]
     (.navigate navigation (name target) (clj->js params)))))

(defn- navigator-factory
  [navigator-class navigation props]
  (js/React.createElement navigator-class
    #js {:navigation navigation
         :screenProps props}))

(defn- create-screen-proxy [class]
  (om/ui
    static field navigationOptions (.-navigationOptions class)
    Object
    (render [this]
      (let [class-factory (om/factory class)
            screen-props  (.. this -props -screenProps)
            navigation    (.. this -props -navigation)
            props         (assoc screen-props :navigation navigation)]
        (class-factory props)))))

(defn- create-navigator-proxy [navigator]
  (om/ui
    static field navigator-proxy? true
    static field router (.-router navigator)
    Object
    (render [this]
      (let [screen-props (.. this -props -screenProps)
            navigation   (.. this -props -navigation)
            props        (om/props this)
            props        (if props props screen-props)]                          
        (navigator-factory navigator navigation props)))))

(defn- transform-route-config
  [[name config]]
  (if (.-navigator-proxy? (:screen config))
    [name config]
    [name (assoc config :screen (create-screen-proxy (:screen config)))]))
  
(defn- transform-route-configs
  [route-configs]
  (let [transformed (mapcat #(transform-route-config %) route-configs)]
    (clj->js (apply hash-map transformed))))

(defn- extract-queries
  [route-configs]
  (let [screens (map (fn [[_ config]] (:screen config)) route-configs)]
    (mapcat #(om/get-query %) screens)))

(defn- create-navigator
  [route-configs factory]
  (let [queries   (into [] (extract-queries route-configs))
        navigator (factory (transform-route-configs route-configs))
        proxy     (create-navigator-proxy navigator)]
    (when (first queries)
      (specify! proxy om/IQuery (query [this] queries)))
    proxy))

(defn stack-navigator
  [routes]
  (create-navigator routes #(StackNavigator %)))

(defn tab-navigator
  [routes]
  (create-navigator routes #(TabNavigator %)))
