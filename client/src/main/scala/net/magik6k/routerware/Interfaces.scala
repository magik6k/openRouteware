package net.magik6k.routerware

import net.magik6k.sjwindow.{WindowManager, Window}

object Interfaces {
  var window: Window = null
  var creating = false

  def view(): Unit = {
    if(creating) return
    if(window == null) {
      creating = true
      WindowManager.create("interfaces", (250, 200), (300, 200), resize = false, w => {
        window = w
        creating = false
        WindowManager.add(window)
      })
    } else if(!window.isOpen) {
      WindowManager.add(window)
    } else {
      window.focus()
    }
  }

}
