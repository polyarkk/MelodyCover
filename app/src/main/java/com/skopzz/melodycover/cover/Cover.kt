package com.skopzz.melodycover.cover

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.skopzz.melodycover.getWindowManager
import com.skopzz.melodycover.preference.PreferenceManager

@SuppressLint("ViewConstructor")
class Cover @SuppressLint("ClickableViewAccessibility") constructor(private val ctx: Context) :
    LinearLayout(ctx) {
    private var x = 0
    private var y = 0
    private var touchX = 0f
    private var touchY = 0f

    private val layoutParams = WindowManager.LayoutParams(
        400, 150,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )

    private val preferenceManager: PreferenceManager

    init {
        layoutParams.x = 0
        layoutParams.y = 0
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP

        preferenceManager = PreferenceManager.instance!!

        setBackgroundColor(preferenceManager.get("pick_color", -0x9596))
        setLayoutParams(
            LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun onTouch(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                x = layoutParams.x
                touchX = e.rawX

                y = layoutParams.y
                touchY = e.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                if (!preferenceManager.get("lock_vertical_position", false)) {
                    layoutParams.x = (x + e.rawX - touchX).toInt()
                }

                if (!preferenceManager.get("lock_horizontal_position", false)) {
                    layoutParams.y = (y + e.rawY - touchY).toInt()
                }

                updateView()
            }

            else -> {}
        }

        return false
    }

    fun setColor(color: Int) {
        setBackgroundColor(color)
    }

    fun setX(x: Int) {
        layoutParams.x = 0

        updateView()
    }

    fun setY(y: Int) {
        layoutParams.y = 0

        updateView()
    }

    private fun updateView() {
        getWindowManager(ctx).updateViewLayout(this, layoutParams)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: Cover? = null
            private set

        @SuppressLint("ClickableViewAccessibility")
        fun create(ctx: Context) {
            instance = Cover(ctx)

            instance!!.setOnTouchListener { _: View, e: MotionEvent ->
                instance!!.onTouch(e)
            }

            getWindowManager(ctx).addView(instance, instance!!.layoutParams)
        }

        fun destroy(ctx: Context) {
            getWindowManager(ctx).removeView(instance)

            instance = null
        }

        fun toggle(ctx: Context): Boolean {
            if (isShowing) {
                destroy(ctx)
            } else {
                create(ctx)
            }

            return isShowing
        }

        val isShowing: Boolean
            get() = instance != null
    }
}
