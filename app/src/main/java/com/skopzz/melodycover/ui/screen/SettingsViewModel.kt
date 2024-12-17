package com.skopzz.melodycover.ui.screen

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skopzz.melodycover.util.get
import com.skopzz.melodycover.util.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(ctx: Context) : ViewModel() {
  class Factory(private val ctx: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SettingsViewModel(ctx) as T
  }

  private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)

  private val _uiState = MutableStateFlow(SettingsUiState(
    sharedPreferences.get("lock_horizontal_position", true),
    sharedPreferences.get("lock_vertical_position", false),
    sharedPreferences.get("gesture_close", true),
  ))

  val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

  fun <T> set(k: String, v: T) {
    sharedPreferences.set(k, v)

    when (k) {
      "lock_horizontal_position" -> _uiState.update { it.copy(lockHorizontalPosition = v as Boolean) }
      "lock_vertical_position" -> _uiState.update { it.copy(lockVerticalPosition = v as Boolean) }
      "gesture_close" -> _uiState.update { it.copy(gestureClose = v as Boolean) }
    }
  }
}