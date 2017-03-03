(ns om-navigate.navigate
  (:require [om.next :as om :refer-macros [ui]]))

(def ReactNavigation (js/require "react-navigation"))
(def StackNavigator (.-StackNavigator ReactNavigation))

(defn navigate-to [c target params]
  (let [props (om/props c)
        navigation (:navigation props)]
    (.navigate navigation (name target) (clj->js params))))

(defn stack-navigator
  [routes]
  (let [proxyfn  (fn [nav class]
                   (om/ui
                     static field navigationOptions
                     (.-navigationOptions class)
                    
                     Object
                     (render [this]
                       (let [inst  (om/factory class)
                             props (assoc (om/props nav) :navigation (.. this -props -navigation))]
                         (inst props)))))

        routefn  (fn [nav [name route]]
                   [name (assoc route :screen (proxyfn nav (:screen route)))])

        routesfn (fn [nav routes]
                   (let [routes' (mapcat #(routefn nav %) routes)]
                     (clj->js (apply hash-map routes'))))

        screens  (map (fn [[_ route]] (:screen route)) routes)
        queries  (mapcat #(om/get-query %) screens)
        ui       (om/ui
                   static om/IQuery
                   (query [this]
                     (into [] queries))
                  
                   Object
                   (render [this]
                     (let [routes'   (routesfn this routes)
                           navigator (om/factory (StackNavigator routes'))]
                       (.log js/console routes')
                       (navigator))))]
    ui))
