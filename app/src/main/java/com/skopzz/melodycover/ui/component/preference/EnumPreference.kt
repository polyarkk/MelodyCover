package com.skopzz.melodycover.ui.component.preference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.skopzz.melodycover.ui.component.dialog.EnumSelectDialog

@Composable
fun <T : Enum<T>> EnumPreference(
  title: @Composable (T) -> Unit,
  summary: @Composable (String) -> Unit,
  v: T,
  onValueChange: (T) -> Unit,
  values: List<T>,
  displays: List<String>,
) {
  var showDialog by remember { mutableStateOf(false) }

  BasePreference(
    title = title,
    summary = {
      val idx = values.indexOf(it);

      if (idx != -1) {
        summary(displays[idx])
      } else {
        summary(it.toString())
      }
    },
    v = v,
    onClick = { showDialog = true }
  )

  if (showDialog) {
    EnumSelectDialog(
      title,
      defaultValue = v,
      values,
      displays,
      onValueChange,
      onDismissRequest = { showDialog = false },
    )
  }
}