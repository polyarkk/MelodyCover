package com.skopzz.melodycover.ui.component.preference

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> BasePreference(
  title: @Composable (T) -> Unit,
  summary: (@Composable (T) -> Unit)? = null,
  v: T,
  leftWidget: (@Composable (T) -> Unit)? = null,
  rightWidget: (@Composable (T) -> Unit)? = null,
  bottomWidget: (@Composable (T) -> Unit)? = null,
  enabled: Boolean = true,
  onClick: (T) -> Unit = {},
) {
  Column(
    modifier = Modifier.Companion.padding(0.dp, 8.dp, 16.dp, 8.dp)
  ) {
    Row(
      modifier = Modifier.Companion
        .height(48.dp)
        .clickable(enabled, onClick = { onClick(v) }),
      verticalAlignment = Alignment.Companion.CenterVertically
    ) {
      if (leftWidget != null) {
        leftWidget(v)
      } else {
        Spacer(Modifier.Companion.size(48.dp))
      }
      Column {
        ProvideTextStyle(
          value = MaterialTheme.typography.titleMedium,
          content = { title(v) }
        )
        if (summary != null) {
          ProvideTextStyle(
            value = MaterialTheme
              .typography
              .bodyMedium
              .copy(
                color = MaterialTheme
                  .colorScheme
                  .onSurfaceVariant
              ),
            content = { summary(v) }
          )
        }
      }
      Spacer(modifier = Modifier.Companion.weight(1f))
      rightWidget?.invoke(v)
    }
    if (bottomWidget != null) {
      Box(modifier = Modifier.Companion.padding(start = 16.dp)) {
        bottomWidget(v)
      }
    }
  }
}