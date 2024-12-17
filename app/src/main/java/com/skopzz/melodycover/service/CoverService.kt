package com.skopzz.melodycover.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.os.IBinder
import androidx.preference.PreferenceManager
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.skopzz.melodycover.cover.Cover
import com.skopzz.melodycover.cover.CoverConfiguration
import com.skopzz.melodycover.cover.loadCoverConfiguration
import com.skopzz.melodycover.util.defaultJson
import com.skopzz.melodycover.util.get
import com.skopzz.melodycover.util.getWindowManager
import com.skopzz.melodycover.util.registerBroadcast
import com.skopzz.melodycover.util.sendBroadcast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString

private const val BROADCAST_COVER_SERVICE_IO = "com.skopzz.melodycover.CoverService.io"
const val BROADCAST_COVER_SERVICE_ON_START = "com.skopzz.melodycover.CoverService.onStart"
const val BROADCAST_COVER_SERVICE_ON_STOP = "com.skopzz.melodycover.CoverService.onStop"
const val BROADCAST_COVER_SERVICE_ON_CONF_UPDATE = "com.skopzz.melodycover.CoverService.onConfUpdate"

class CoverService : Service() {
  lateinit var instance: Cover
  lateinit var sharedPreferences: SharedPreferences
  lateinit var unregisterBroadcast: () -> Unit

  private var x = 0
  private var y = 0
  private var touchX = 0f
  private var touchY = 0f
  private var clickCount = 0
  private var prevClickMs = 0L;

  lateinit var windowLayoutParams: WindowManager.LayoutParams

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onCreate() {
    super.onCreate()

    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    instance = Cover(applicationContext, getConfig())

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

    unregisterBroadcast = registerBroadcast(BROADCAST_COVER_SERVICE_IO) { ctx, intent ->
      val setX = intent?.getIntExtra("setX", -1)
      if (setX != null && setX != -1) {
        windowLayoutParams.x = 0
      }

      val setY = intent?.getIntExtra("setY", -1)
      if (setY != null && setY != -1) {
        windowLayoutParams.y = 0
      }

      if (intent?.getIntExtra("reload", -1) != -1) {
        instance.update(getConfig())
        windowLayoutParams.width = instance.layoutParams.width
        windowLayoutParams.height = instance.layoutParams.height

        sendBroadcast(BROADCAST_COVER_SERVICE_ON_CONF_UPDATE) {
          it.putExtra("conf", defaultJson().encodeToString(instance.configuration))
        }
      }

      updateView()
    }

    sendBroadcast(BROADCAST_COVER_SERVICE_ON_START) {
      it.putExtra("conf", defaultJson().encodeToString(instance.configuration))
    }
  }

  private fun getConfig() = loadCoverConfiguration(sharedPreferences.get("cover_config", "default")) ?: CoverConfiguration()

  private fun onTouch(e: MotionEvent) {
    when (e.action) {
      MotionEvent.ACTION_DOWN -> {
        if (sharedPreferences.get("gesture_close", true)) {
          if (handleTripleTap()) {
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
        if (!sharedPreferences.get("lock_horizontal_position", true)) {
          windowLayoutParams.x = (x + e.rawX - touchX).toInt()
        }

        if (!sharedPreferences.get("lock_vertical_position", false)) {
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
    isRunning.value = false
    unregisterBroadcast()

    sendBroadcast(BROADCAST_COVER_SERVICE_ON_STOP) { }
  }

  fun updateView() {
    getWindowManager().updateViewLayout(instance, windowLayoutParams)
  }

  private fun handleTripleTap(): Boolean {
    clickCount++;

    if (prevClickMs == 0L) {
      prevClickMs = System.currentTimeMillis()

      return false
    }

    val clickMs = System.currentTimeMillis()

    if (clickMs - prevClickMs > 500) {
      clickCount = 1
      prevClickMs = clickMs

      return false
    } else if (clickCount == 3) {
      clickCount = 0

      return true
    }

    prevClickMs = clickMs

    return false
  }

  companion object {
    // any better idea?
    var isRunning = MutableStateFlow(false)

    fun start(ctx: Context) {
      ctx.startService(Intent(ctx, CoverService::class.java))
    }

    fun stop(ctx: Context) {
      ctx.stopService(Intent(ctx, CoverService::class.java))
    }

    fun toggle(ctx: Context) {
      if (isRunning.value) {
        stop(ctx)
      } else {
        start(ctx)
      }
    }

    fun sendIo(ctx: Context, cmd: String, value: Int) {
      ctx.sendBroadcast(BROADCAST_COVER_SERVICE_IO) { intent ->
        intent.putExtra(cmd, value)
      }
    }
  }
}