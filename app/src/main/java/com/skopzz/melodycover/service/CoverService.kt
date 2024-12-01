package com.skopzz.melodycover.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.skopzz.melodycover.cover.Cover
import com.skopzz.melodycover.preference.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow

class CoverService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        if (PreferenceManager.instance == null) {
            PreferenceManager.init(this)
        }

        Cover.create(this)
        Log.i("cover_service", "cover service is now running")
        isRunning.value = true
    }

    override fun onDestroy() {
        super.onDestroy()

        Cover.destroy(this)
        Log.i("cover_service", "cover service is now stopping")
        isRunning.value = false
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
    }
}