(ns om-navigate.sections
  (:require [om-navigate.sections.simple-stack]
            [om-navigate.sections.simple-tabs]
            [om-navigate.sections.drawer]))

(def SimpleStack om-navigate.sections.simple-stack/SimpleStack)
(def SimpleTabs om-navigate.sections.simple-tabs/SimpleTabs)
(def Drawer om-navigate.sections.drawer/Drawer)