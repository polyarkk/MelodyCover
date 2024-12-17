package com.skopzz.melodycover.ui.component.preference

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.skopzz.melodycover.ui.component.dialog.ColorPickerDialog

@Composable
fun SwitchPreference(
  title: @Composable (Boolean) -> Unit,
  summary: (@Composable (Boolean) -> Unit)? = null,
  v: Boolean,
  onValueChange: (Boolean) -> Unit,
) {
  var showDialog by remember { mutableStateOf(false) }

  BasePreference(
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