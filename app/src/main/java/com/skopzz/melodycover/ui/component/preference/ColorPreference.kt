package com.skopzz.melodycover.ui.component.preference

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.skopzz.melodycover.ui.component.dialog.ColorPickerDialog

@Composable
fun ColorPreference(
    title: @Composable (ULong) -> Unit,
    summary: @Composable (ULong) -> Unit,
    v: ULong,
    onValueChange: (ULong) -> Unit,
) {
  var showDialog by remember { mutableStateOf(false) }

  BasePreference(
    title,
    summary,
    v,
    rightWidget = {
        AlphaTile(
            modifier = Modifier.Companion
                .size(36.dp)
                .clip(RoundedCornerShape(6.dp)),
            selectedColor = Color(v shl 32)
        )
    },
    onClick = {
      showDialog = true
    }
  )

  if (showDialog) {
      ColorPickerDialog(
          v,
          onDismissRequest = { showDialog = false },
          onConfirmRequest = { onValueChange(it) }
      )
  }
}