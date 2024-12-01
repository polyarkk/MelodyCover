package com.skopzz.melodycover

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.util.Log
import android.view.WindowManager

@JvmStatic
fun getWindowManager(ctx: Context): WindowManager {
    return ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
}