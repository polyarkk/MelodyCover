package com.skopzz.melodycover.ui.component.preference

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PreferenceCategory(s: String) {
  ProvideTextStyle(
    MaterialTheme.typography.labelLarge.copy(
      color = MaterialTheme.colorScheme.primary
    )
  ) {
    Box(
      contentAlignment = Alignment.BottomStart,
      modifier = Modifier
        .height(36.dp)
        .fillMaxWidth()
        .padding(top = 4.dp)
    ) {
      Text(text = s, modifier = Modifier.padding(start = 48.dp))
    }
  }
}

@Preview(showBackground = true)
@Composable
fun P() {
  Column {
    PreferenceCategory("114514")
    SwitchPreference(
      title = { Text("Switch") },
      summary = { Text("Summary") },
      v = true,
      onValueChange = { },
    )
    SwitchPreference(
      title = { Text("Switch") },
      summary = { Text("Summary") },
      v = true,
      onValueChange = { },
    )
  }
}