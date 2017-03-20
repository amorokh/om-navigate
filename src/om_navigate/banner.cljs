(ns om-navigate.banner
  (:require [om.next :as om :refer-macros [defui]]
            [om-navigate.elements :as e]))

(def nav-logo (js/require "./images/NavLogo.png"))

(def styles
  {:banner
     {:backgroundColor "#673ab7"
      :flexDirection "row"
      :alignItems "center"
      :padding 16
      :marginTop 20}
   :image
     {:width 36
      :height 36
      :resizeMode "contain"
      :tintColor "#fff"
      :margin 8}
   :title
     {:fontSize 18
      :fontWeight "200"
      :color "#fff"
      :margin 8}})

(defui Banner
  Object
  (render [this]
    (e/view {:style (:banner styles)}
      (e/image {:source nav-logo :style (:image styles)})
      (e/text {:style (:title styles)} "React Navigation Examples"))))

