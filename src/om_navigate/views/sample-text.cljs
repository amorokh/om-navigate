(ns om-navigate.views.sample-text
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]))

;; =============================================================================
;; SampleText

(defui SampleText
  Object
  (render [this]
    (let [{:keys [text]} (om/props this)]
      (e/text #js {:style #js {:margin 14}} text))))

(def sample-text (om/factory SampleText))
