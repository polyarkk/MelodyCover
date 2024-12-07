package com.skopzz.melodycover.ui.component.preference

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction

@Composable
fun TextFieldPreference(
  title: @Composable (String) -> Unit,
  summary: (@Composable (String) -> Unit)? = null,
  v: String,
  onValueChange: (String) -> Unit,
) {
  BasePreference(
    title,
    summary,
    v,
    rightWidget = {
      BasicTextField(
        value = v,
        onValueChange,
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
      )
    },
  )
}