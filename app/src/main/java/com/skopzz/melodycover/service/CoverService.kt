package com.skopzz.melodycover.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.skopzz.melodycover.cover.Cover
import com.skopzz.melodycover.cover.TapTimer
import com.skopzz.melodycover.util.get
import com.skopzz.melodycover.util.getWindowManager
import com.skopzz.melodycover.util.registerBroadcast
import com.skopzz.melodycover.util.sendBroadcast
import kotlinx.coroutines.flow.MutableStateFlow

class CoverService : Service() {
  lateinit var instance: Cover
  lateinit var sharedPreferences: SharedPreferences
  lateinit var unregisterBroadcast: () -> Unit

  private var x = 0
  private var y = 0
  private var touchX = 0f
  private var touchY = 0f
  private val tapTimer = TapTimer()

  lateinit var windowLayoutParams: WindowManager.LayoutParams

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onCreate() {
    super.onCreate()

    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    instance = Cover(applicationContext)

    instance.setOnTouchListener { _: View, e: MotionEvent ->
      onTouch(e)

      return@setOnTouchListener false
    }

    windowLayoutParams = WindowManager.LayoutParams(
      instance.layoutParams.width, instance.layoutParams.height,
      WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
      WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
      PixelFormat.TRANSLUCENT
    )

    windowLayoutParams.x = 0
    windowLayoutParams.y = 0
    windowLayoutParams.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP

    getWindowManager().addView(instance, windowLayoutParams)

    Log.i("cover_service", "cover service is now running")
    isRunning.value = true

    unregisterBroadcast = registerBroadcast("com.skopzz.melodycover.CoverService.io") { ctx, intent ->
      val setX = intent?.getIntExtra("setX", -1)
      if (setX != null && setX != -1) {
        windowLayoutParams.x = 0
      }

      val setY = intent?.getIntExtra("setY", -1)
      if (setY != null && setY != -1) {
        windowLayoutParams.y = 0
      }

      updateView()
    }
  }

  private fun onTouch(e: MotionEvent) {
    when (e.action) {
      MotionEvent.ACTION_DOWN -> {
        if (sharedPreferences.get("gesture_close", true)) {
          if (tapTimer.handleTripleTap()) {
            stopSelf()

            return
          }
        }

        x = windowLayoutParams.x
        touchX = e.rawX

        y = windowLayoutParams.y
        touchY = e.rawY
      }

      MotionEvent.ACTION_MOVE -> {
        if (!sharedPreferences.get("lock_vertical_position", false)) {
          windowLayoutParams.x = (x + e.rawX - touchX).toInt()
        }

        if (!sharedPreferences.get("lock_horizontal_position", false)) {
          windowLayoutParams.y = (y + e.rawY - touchY).toInt()
        }

        updateView()
      }

      else -> {}
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    getWindowManager().removeView(instance)
    Log.i("cover_service", "cover service is now stopping")
    isRunning.value = false
    unregisterBroadcast()
  }

  fun updateView() {
    getWindowManager().updateViewLayout(instance, windowLayoutParams)
  }

  companion object {
    var isRunning = MutableStateFlow(false)

    fun start(ctx: Context) {
      ctx.startService(Intent(ctx, CoverService::class.java))
    }

    fun stop(ctx: Context) {
      ctx.stopService(Intent(ctx, CoverService::class.java))
    }

    fun toggle(ctx: Context): Boolean {
      if (isRunning.value) {
        stop(ctx)
        return false
      } else {
        start(ctx)
        return true
      }
    }

    fun sendIo(ctx: Context, cmd: String, value: Int) {
      ctx.sendBroadcast("com.skopzz.melodycover.CoverService.io") { intent ->
        intent.putExtra(cmd, value)
      }
    }

    fun sendIo(ctx: Context, cmd: String, value: String?) {
      ctx.sendBroadcast("com.skopzz.melodycover.CoverService.io") { intent ->
        intent.putExtra(cmd, value)
      }
    }
  }
}