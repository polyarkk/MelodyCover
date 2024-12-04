package com.skopzz.melodycover.cover

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

@SuppressLint("ViewConstructor", "SetTextI18n")
class Cover(
  val ctx: Context,
  var configuration: CoverConfiguration = CoverConfiguration(),
  editMode: Boolean = false, // todo
) : LinearLayout(ctx) {
  val layoutParams: LayoutParams = LayoutParams(
    configuration.width,
    configuration.height
  )

  var editModeTextView: TextView? = null;

  init {
    setLayoutParams(layoutParams)
    update(configuration)
    if (editMode) {
      editModeTextView = TextView(ctx).apply {
        text = "${this@Cover.layoutParams.width}x${this@Cover.layoutParams.height}"
        textSize = 20f
        setTextColor(Color.BLACK)
        setBackgroundColor(Color.WHITE)
        setTypeface(null, Typeface.BOLD)
        setPadding(8, 8, 8, 8)
        layoutParams = LayoutParams(
          LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT
        )
      }

      addView(editModeTextView)
    }
  }

  fun update(conf: CoverConfiguration) {
    when (conf.coverType) {
      CoverType.COLOR -> {
        if (background == null
          || background !is ColorDrawable
          || conf.color != configuration.color
        ) {
          background = ColorDrawable(conf.color.toInt())
        }

        layoutParams.width = conf.width
        layoutParams.height = conf.height
      }

      CoverType.IMAGE -> {
        val drawable = if (background == null
          || background !is CoverImageDrawable
          || conf.imageUpdateMs != configuration.imageUpdateMs
        ) {
          CoverImageDrawable(
            Drawable.createFromPath(conf.imagePath)
              ?: ResourcesCompat.getDrawable(ctx.resources, conf.imageId, null)!!
          )
        } else {
          background
        } as CoverImageDrawable

        drawable.apply {
          scale = conf.imageScale
          setLayerInset(0, conf.imageInsetR, conf.imageInsetB, conf.imageInsetL, conf.imageInsetT)
          update()
        }

        layoutParams.width = ((drawable.width - conf.imageInsetL - conf.imageInsetR) * conf.imageScale).toInt()
        layoutParams.height = ((drawable.height - conf.imageInsetT - conf.imageInsetB) * conf.imageScale).toInt()

        background = drawable
      }
    }

    if (editModeTextView != null) {
      editModeTextView!!.text = "${layoutParams.width}x${layoutParams.height}"
    }

    configuration = conf
    requestLayout()
  }
}
