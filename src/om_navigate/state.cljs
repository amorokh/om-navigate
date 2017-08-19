(ns om-navigate.state
  (:require [om.next :as om]
            [om-navigate.navigate :as nav]
            [re-natal.support :as sup]
            [cljs.pprint :as pp]))

(defonce app-state 
  (atom {:simple-stack/home {:title "Welcome"
                             :header "Home screen"}
         :simple-stack/profile {:name "Jane"
                                :editing? false}}))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state ast parser query] :as env} k p]
  (if (nav/screen-query? query)
    {:value (parser (dissoc env :ast) (:query ast))}
    {:value :not-found}))

(defmethod read :simple-stack/home
  [{:keys [state]} _ _]
  {:value (:simple-stack/home @state)})

(defmethod read :simple-stack/profile
  [{:keys [state]} _ _]
  {:value (:simple-stack/profile @state)})

(defmulti mutate om/dispatch)

(defmethod mutate 'simple-stack/toggle-profile-editing
  [{:keys [state] :as env} _ _]
  (let [editing? (-> @state :simple-stack/profile :editing?)]
    {:action #(swap! state update-in [:simple-stack/profile :editing?] not)}))

(defonce reconciler
         (om/reconciler
           {:state        app-state
            :parser       (om/parser {:read read :mutate mutate})
            :root-render  sup/root-render
            :root-unmount sup/root-unmount}))
