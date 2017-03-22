(ns om-navigate.sections.sample-text
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]))

(def styles
  {:sample-text
     {:margin 14}})
  
(defui SampleText
  Object
  (render [this]
    (let [{:keys [text]} (om/props this)]
      (e/text {:style (:sample-text styles)} text))))
