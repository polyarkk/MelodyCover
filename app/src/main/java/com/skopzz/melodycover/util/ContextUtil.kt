package com.skopzz.melodycover.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.view.WindowManager
import java.io.File
import java.io.FileOutputStream

@JvmStatic
fun Context.getWindowManager(): WindowManager {
  return getSystemService(Context.WINDOW_SERVICE) as WindowManager
}

fun Context.registerBroadcast(action: String, onReceive: (Context?, Intent?) -> Unit): () -> Unit {
  val filter = IntentFilter(action)

  val receiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      onReceive(context, intent)
    }
  }

  if (Build.VERSION.SDK_INT >= 33) {
    registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
  } else {
    registerReceiver(receiver, filter)
  }

  return {
    unregisterReceiver(receiver)
  };
}

fun Context.sendBroadcast(
  action: String,
  extra: ((Intent) -> Unit)?
) {
  val intent = Intent()

  intent.setAction(action)

  if (extra != null) {
    extra(intent)
  }

  sendBroadcast(intent)
}

fun getFileBitmapSize(imagePath: String): Pair<Int, Int> {
  val bitmapOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true };

  BitmapFactory.decodeFile(imagePath, bitmapOptions);

  return bitmapOptions.outWidth.to(bitmapOptions.outHeight)
}

fun Context.getResourceBitmapSize(id: Int): Pair<Int, Int> {
  val bitmapOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true };

  BitmapFactory.decodeResource(resources, id, bitmapOptions);

  return bitmapOptions.outWidth.to(bitmapOptions.outHeight)
}

fun Context.copyFileFromUri(
  uri: Uri,
  toFileName: String,
): File {
  val inputStream = contentResolver.openInputStream(uri)
  val outputFile = File(getExternalFilesDir(null)!!, toFileName)
  var outputStream: FileOutputStream? = null

  if (outputFile.exists()) {
    outputFile.delete()
  } else {
    outputFile.parentFile?.mkdirs()
  }

  return try {
    outputStream = FileOutputStream(outputFile)
    inputStream?.copyTo(outputStream)
    outputFile
  } finally {
    inputStream?.close()
    outputStream?.close()
  }
}