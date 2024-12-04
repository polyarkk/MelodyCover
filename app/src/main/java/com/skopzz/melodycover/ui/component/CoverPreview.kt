package com.skopzz.melodycover.ui.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.viewinterop.AndroidView
import com.skopzz.melodycover.cover.Cover
import com.skopzz.melodycover.cover.CoverConfiguration
import com.skopzz.melodycover.util.defaultJson
import kotlinx.serialization.encodeToString

@Composable
fun CoverPreview(conf: CoverConfiguration) {
  AndroidView(
    factory = {
      Cover(it, conf, true)
    },
    update = { cover ->
      Log.i("composed_cover", "we should update the cover now! new conf: ${defaultJson().encodeToString(conf)}")
      cover.update(conf)
    },
    modifier = Modifier
      .clipToBounds()
  )
}