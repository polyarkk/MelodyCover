package com.skopzz.melodycover.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skopzz.melodycover.cover.CoverConfiguration
import com.skopzz.melodycover.cover.loadCoverConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConfigEditViewModel(key: String) : ViewModel() {
  class Factory(private val key: String) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ConfigEditViewModel(key) as T
  }

  private val _uiState = MutableStateFlow(ConfigEditUiState())

  val uiState: StateFlow<ConfigEditUiState> = _uiState.asStateFlow()

  private var conf = loadCoverConfiguration(key) ?: throw NullPointerException("conf $key not found")

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
