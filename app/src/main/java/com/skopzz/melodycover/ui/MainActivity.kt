package com.skopzz.melodycover.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.skopzz.melodycover.cover.CoverConfiguration
import com.skopzz.melodycover.cover.existsCoverConfiguration
import com.skopzz.melodycover.cover.saveCoverConfiguration
import com.skopzz.melodycover.ui.theme.MelodyCoverTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val requestPermissions = registerForActivityResult(RequestMultiplePermissions()) { results ->
      results.forEach { (key: String, isGranted: Boolean) ->
        Log.d(TAG, "$key: $isGranted")

        if (!isGranted) {
          AlertDialog.Builder(this)
            .setMessage("未授予图片读取权限，将无法使用图片上隐条")
            .setPositiveButton("确认", DialogInterface.OnClickListener { dialog, which -> })
            .show()
        }
      }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED))
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
    } else {
      requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
    }

    if (!existsCoverConfiguration("default")) {
      saveCoverConfiguration(CoverConfiguration())
    }

    enableEdgeToEdge()
    setContent {
      MelodyCoverTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          App()
        }
      }
    }
  }
}

