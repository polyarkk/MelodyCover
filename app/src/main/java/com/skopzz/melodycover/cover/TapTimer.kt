package com.skopzz.melodycover.cover

class TapTimer {
  var count = 0
  var prevClickMs = 0L;

  fun handleTripleTap(): Boolean {
    count++;

    if (prevClickMs == 0L) {
      prevClickMs = System.currentTimeMillis()

      return false
    }

    val clickMs = System.currentTimeMillis()

    if (clickMs - prevClickMs > 1000) {
      count = 0
      prevClickMs = clickMs

      return false
    } else if (count == 3) {
      count = 0

      return true
    }

    prevClickMs = clickMs

    return false
  }
}