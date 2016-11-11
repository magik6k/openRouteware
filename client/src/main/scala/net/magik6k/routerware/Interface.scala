package net.magik6k.routerware

import net.magik6k.sjwindow.WindowManager

object Interface {
  def open(interface: String): Unit = {
    WindowManager.create(s"interface/$interface", (400, 400), null, resize = true, w => {
      WindowManager.add(w)
    })
  }
}
