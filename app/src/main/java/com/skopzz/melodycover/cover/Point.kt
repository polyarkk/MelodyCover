package com.skopzz.melodycover.cover

import kotlinx.serialization.Serializable

/**
 * yep, a serializable point class
 */
@Serializable
class Point {
  constructor(x: Int, y: Int) {
    this.x = x
    this.y = y
  }

  var x: Int = 0
  var y: Int = 0
}