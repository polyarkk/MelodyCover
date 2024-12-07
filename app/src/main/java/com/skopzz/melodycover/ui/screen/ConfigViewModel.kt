package com.skopzz.melodycover.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skopzz.melodycover.COVER_DIR
import com.skopzz.melodycover.cover.CoverConfiguration
import com.skopzz.melodycover.cover.getImagePath
import com.skopzz.melodycover.cover.loadCoverConfiguration
import com.skopzz.melodycover.cover.saveCoverConfiguration
import com.skopzz.melodycover.service.CoverService
import com.skopzz.melodycover.util.defaultJson
import com.skopzz.melodycover.util.get
import com.skopzz.melodycover.util.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("StaticFieldLeak")
class ConfigViewModel(ctx: Context) : ViewModel() {
  class Factory(private val ctx: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ConfigViewModel(ctx) as T
  }

  private val _uiState = MutableStateFlow(ConfigUiState())

  val uiState: StateFlow<ConfigUiState> = _uiState.asStateFlow()

  private val pref = PreferenceManager.getDefaultSharedPreferences(ctx)

  private var confKeyToDelete: String? = null

  init {
    loadList()
  }

  private fun loadList() {
    val coverKvs = mutableMapOf<String, String>()

    for (f in File(COVER_DIR).listFiles()!!) {
      if (f.isDirectory && f.name != confKeyToDelete) {
        val conf = defaultJson().decodeFromString(File(f, "config.json").readText()) as CoverConfiguration

        coverKvs[conf.key] = conf.name
      }
    }

    _uiState.update {
      it.copy(
        configs = coverKvs,
        loadedConfigKey = pref.get("cover_config", "default")
      )
    }
  }

  fun copyConfiguration(key: String): String {
    val conf = loadCoverConfiguration(key) ?: throw NullPointerException("conf $key not found")

    val image = File(conf.getImagePath())

    conf.name += " (Copy ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())})"
    conf.key = System.currentTimeMillis().toString()

    saveCoverConfiguration(conf)

    if (image.exists()) {
      image.copyTo(File(conf.getImagePath()), true)
    }

    return conf.key
  }

  fun selectConfiguration(ctx: Context, key: String) {
    pref.set("cover_config", key)

    if (CoverService.isRunning.value) {
      CoverService.sendIo(ctx, "reload", 1)
    }

    _uiState.update {
      it.copy(loadedConfigKey = key)
    }
  }

  fun showDeleteDialog(kv: Pair<String, String>) {
    _uiState.update {
      it.copy(deleting = kv)
    }
  }

  fun dismissDeleteDialog() {
    _uiState.update {
      it.copy(deleting = null)
    }
  }

  fun scheduleDeleteConfiguration(k: String) {
    confKeyToDelete = k

    loadList()
  }

  fun undoDeleteConfiguration() {
    confKeyToDelete = null

    loadList()
  }

  fun deleteConfiguration() {
    if (confKeyToDelete == null) {
      return
    }

    File("$COVER_DIR/$confKeyToDelete").deleteRecursively()

    confKeyToDelete = null

    loadList()
  }
}