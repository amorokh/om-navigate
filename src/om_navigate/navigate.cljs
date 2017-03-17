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
            screen-props   (.. this -props -screenProps)
            navigation     (.. this -props -navigation)
            props          (assoc screen-props :navigation navigation)]
        (screen-factory props)))))

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

(defn- transform-routes
  [routes]
  (reduce-kv
    #(assoc %1 %2
       (let [{:keys [screen]} %3]
         (if (.-navigator-proxy? screen) 
           %3 
           {:screen (create-screen-proxy screen)})))
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

(defn stack-navigator
  [routes]
  (create-navigator routes #(StackNavigator %)))

(defn tab-navigator
  [routes]
  (create-navigator routes #(TabNavigator %)))
