package models

object SeqProvider {
  private var cur: Long = 0

  def provide(): Long = {
    cur = cur + 1
    cur
  }
}
