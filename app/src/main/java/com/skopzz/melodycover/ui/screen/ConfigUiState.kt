package com.skopzz.melodycover.ui.screen

data class ConfigUiState(
  val loadedConfigKey: String = "default",
  val configs: Map<String, String> = mapOf(),
  val deleting: Pair<String, String>? = null,
)
