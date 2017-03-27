(ns om-navigate.screens
  (:require [om-navigate.screens.simple-stack]
            [om-navigate.screens.simple-tabs]
            [om-navigate.screens.drawer]
            [om-navigate.screens.custom-tabs]))

(def SimpleStack om-navigate.screens.simple-stack/SimpleStack)
(def SimpleTabs om-navigate.screens.simple-tabs/SimpleTabs)
(def Drawer om-navigate.screens.drawer/Drawer)
(def CustomTabs om-navigate.screens.custom-tabs/CustomTabs)