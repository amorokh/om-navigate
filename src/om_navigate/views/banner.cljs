(ns om-navigate.views.banner
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]))

;; =============================================================================
;; Logo

(def nav-logo (js/require "./images/NavLogo.png"))

;; =============================================================================
;; Styles

(def styles
  {:banner #js {:backgroundColor "#673ab7"
                :flexDirection "row"
                :alignItems "center"
                :padding 16
                :marginTop 20}

   :image #js {:width 36
               :height 36
               :resizeMode "contain"
               :tintColor "#fff"
               :margin 8}

   :title #js {:fontSize 18
               :fontWeight "200"
               :color "#fff"
               :margin 8}})

;; =============================================================================
;; Banner

(defui Banner
  Object
  (render [this]
    (e/view #js {:style (styles :banner)}
      (e/image #js {:source nav-logo :style (styles :image)})
      (e/text #js {:style (styles :title)} "React Navigation Examples"))))

(def banner (om/factory Banner))
