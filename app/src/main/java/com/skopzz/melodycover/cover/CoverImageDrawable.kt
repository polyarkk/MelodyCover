package com.skopzz.melodycover.cover

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.Gravity

class CoverImageDrawable(drawable: Drawable) : LayerDrawable(
  arrayOf(drawable)
) {
  val drawable = getDrawable(0) as BitmapDrawable

  val width get() = drawable.bitmap.width

  val height get() = drawable.bitmap.height

  var scale = 1f

  init {
    Log.i("cover_img_drawable", "image w*h: $width * $height")
    setLayerGravity(0, Gravity.CENTER)
    update()
  }

  fun update() {
    setLayerSize(0, (width * scale).toInt(), (height * scale).toInt())
  }
}