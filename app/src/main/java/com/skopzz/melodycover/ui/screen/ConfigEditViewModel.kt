package com.skopzz.melodycover.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.lifecycle.ViewModel
import com.skopzz.melodycover.cover.CoverConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConfigEditViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(ConfigEditUiState())

  val uiState: StateFlow<ConfigEditUiState> = _uiState.asStateFlow()

  private var conf = CoverConfiguration()

  init {
    updateConfToUiState()
  }

  fun updateConfiguration(editor: (CoverConfiguration) -> CoverConfiguration) {
    conf = editor(conf)

    updateConfToUiState()
  }

  private fun updateConfToUiState() {
    _uiState.update { it.copy(conf) }
  }
}
