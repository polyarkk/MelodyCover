package com.skopzz.melodycover.service

import android.service.quicksettings.Tile.STATE_ACTIVE
import android.service.quicksettings.Tile.STATE_INACTIVE
import android.service.quicksettings.TileService
import kotlin.time.Duration

class McTileService: TileService() {
    override fun onCreate() {
        super.onCreate()
    }

    // Called when the user adds your tile.
    override fun onTileAdded() {
        super.onTileAdded()

        updateActive()
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

        qsTile.state = if (CoverService.toggle(this)) STATE_ACTIVE else STATE_INACTIVE
        qsTile.updateTile()
    }

    // Called when the user removes your tile.
    override fun onTileRemoved() {
        super.onTileRemoved()
    }

    private fun updateActive() {
        qsTile.state = if (CoverService.isRunning.value) STATE_ACTIVE else STATE_INACTIVE
        qsTile.updateTile()
    }
}