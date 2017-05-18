(ns om-navigate.navigate
  (:require [om.next :as om :refer-macros [ui]]))

(def ReactNavigation (js/require "react-navigation"))

(def StackNavigator (.-StackNavigator ReactNavigation))
(def TabNavigator (.-TabNavigator ReactNavigation))
(def DrawerNavigator (.-DrawerNavigator ReactNavigation))

(def TabRouter (.-TabRouter ReactNavigation))

(defn navigate-to 
  ([c target] (navigate-to c target {}))
  ([c target params]
   (let [navigation (or (.. c -props -navigation) (:navigation (om/props c)))]
     (.navigate navigation (name target) (clj->js params)))))

(defn navigate-back [c]
  (let [navigation (or (.. c -props -navigation) (:navigation (om/props c)))]
    (.goBack navigation nil)))

(defn- render-screen [screen-comp navigation props parent]
  (let [screen-factory (om/factory screen-comp)
        reconciler#    (om/get-reconciler parent)
        depth#         (inc (om/depth parent))
        shared#        (om/shared parent)
        instrument#    (om/instrument parent)]
    (binding [om/*reconciler* reconciler#
              om/*depth*      depth#
              om/*shared*     shared#
              om/*instrument* instrument#
              om/*parent*     parent]
      (.cloneElement js/React (screen-factory props) #js {:navigation navigation}))))

(defn- create-screen-proxy [screen]
  (om/ui
    static field router (.-router screen)
    static field navigationOptions (.-navigationOptions screen)
    Object
    (shouldComponentUpdate [this _ _] true)
    (render [this]
      (let [navigation     (.. this -props -navigation)
            screen-props   (.. this -props -screenProps)
            parent         (:parent screen-props)
            props          (:props screen-props)]
        (render-screen screen navigation props parent)))))

(defn- render-navigator [nav-comp navigation props parent]
  (js/React.createElement nav-comp #js {:navigation navigation :screenProps {:props props :parent parent}}))

(defn- create-navigator-proxy [navigator name]
  (om/ui
    static field router (.-router navigator)
    Object
    (shouldComponentUpdate [this _ _] true)
    (render [this]
      (set! (.-proxy-for this) name)
      (let [om-props     (om/props this)
            screen-props (.. this -props -screenProps)
            navigation   (.. this -props -navigation)
            props        (or om-props screen-props)]
        (render-navigator navigator navigation props this)))))

(defn- transform-routes
  [routes]
  (reduce-kv
    (fn [acc k {:keys [screen] :as v}]
      (assoc acc k
         (assoc v :screen (create-screen-proxy screen))))
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
        proxy     (create-navigator-proxy navigator name)]
    (when (seq queries)
      (specify! proxy om/IQuery (query [this] queries)))
    proxy))

(defn create-stack-navigator
  ([routes] (create-stack-navigator routes {}))
  ([routes cfg]
   (create-navigator routes #(StackNavigator % (clj->js cfg)))))

(defn create-tab-navigator
  ([routes] (create-tab-navigator routes {}))
  ([routes cfg]
   (create-navigator routes #(TabNavigator % (clj->js cfg)))))

(defn create-drawer-navigator
  ([routes] (create-drawer-navigator routes {}))
  ([routes cfg]
   (create-navigator routes #(DrawerNavigator % (clj->js cfg)))))

(defn create-custom-navigator
  ([comp router-factory routes] (create-custom-navigator comp router-factory routes {}))
  ([comp router-factory routes cfg]
   (create-navigator
     routes 
     (fn [routes']
       (let [router    (router-factory routes' cfg)
             navigator (.createNavigator ReactNavigation router)]
         (.createNavigationContainer ReactNavigation (navigator comp)))))))

(defn create-tab-router
  ([routes] (create-tab-router routes {}))
  ([routes cfg]
   (TabRouter (clj->js routes) (clj->js cfg))))

(defn add-navigation-helpers 
  [src]
  (.addNavigationHelpers ReactNavigation src))

