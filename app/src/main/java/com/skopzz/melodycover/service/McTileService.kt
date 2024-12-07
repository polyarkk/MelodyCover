package com.skopzz.melodycover.service

import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import android.service.quicksettings.Tile.STATE_ACTIVE
import android.service.quicksettings.Tile.STATE_INACTIVE
import android.service.quicksettings.TileService
import com.skopzz.melodycover.cover.CoverConfiguration
import com.skopzz.melodycover.util.defaultJson
import com.skopzz.melodycover.util.registerBroadcast

class McTileService : TileService() {
  private lateinit var pref: SharedPreferences
  private lateinit var disposeOnStartBroadcast: () -> Unit
  private lateinit var disposeOnStopBroadcast: () -> Unit
  private lateinit var disposeOnConfUpdateBroadcast: () -> Unit

  var conf: CoverConfiguration? = null

  override fun onCreate() {
    super.onCreate()

    pref = PreferenceManager.getDefaultSharedPreferences(this)

    disposeOnStartBroadcast = registerBroadcast(BROADCAST_COVER_SERVICE_ON_START) { ctx, intent ->
      conf = defaultJson().decodeFromString(intent!!.getStringExtra("conf")!!)
      updateActive(true)
    }

    disposeOnStopBroadcast = registerBroadcast(BROADCAST_COVER_SERVICE_ON_STOP) { ctx, intent ->
      updateActive(false)
    }

    disposeOnConfUpdateBroadcast = registerBroadcast(BROADCAST_COVER_SERVICE_ON_CONF_UPDATE) { ctx, intent ->
      conf = defaultJson().decodeFromString(intent!!.getStringExtra("conf")!!)
      updateActive(true)
    }
  }

  override fun onDestroy() {
    super.onDestroy()

    disposeOnStartBroadcast()
    disposeOnStopBroadcast()
    disposeOnConfUpdateBroadcast()
  }

  // Called when the user adds your tile.
  override fun onTileAdded() {
    super.onTileAdded()
  }

  // Called when your app can update your tile.
  override fun onStartListening() {
    super.onStartListening()

    updateActive()
  }

  // Called when your app can no longer update your tile.
  override fun onStopListening() {
    super.onStopListening()

    updateActive()
  }

  // Called when the user taps on your tile in an active or inactive state.
  override fun onClick() {
    super.onClick()

    CoverService.toggle(this)
  }

  // Called when the user removes your tile.
  override fun onTileRemoved() {
    super.onTileRemoved()
  }

  private fun updateActive(v: Boolean = CoverService.isRunning.value) {
    qsTile.state = if (v) STATE_ACTIVE else STATE_INACTIVE

    when (qsTile.state) {
      STATE_ACTIVE -> {
        qsTile.label = conf?.name ?: "上隐条"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          qsTile.subtitle = "上隐条"
        }
      }
      STATE_INACTIVE -> {
        qsTile.label = "上隐条"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          qsTile.subtitle = null
        }
      }
    }

    qsTile.updateTile()
  }
}