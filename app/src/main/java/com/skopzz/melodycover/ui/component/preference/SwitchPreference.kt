package com.skopzz.melodycover.ui.component.preference

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun SwitchPreference(
  title: @Composable (Boolean) -> Unit,
  summary: (@Composable (Boolean) -> Unit)? = null,
  v: Boolean,
  onValueChange: (Boolean) -> Unit,
) {
  var showDialog by remember { mutableStateOf(false) }

  Preference(
    title,
    summary,
    v,
    rightWidget = {
      Switch(
        checked = v,
        onCheckedChange = onValueChange,
      )
    },
  )
}