package com.skopzz.melodycover.ui.component.preference

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import com.skopzz.melodycover.util.toSpecificNumber

@Composable
inline fun <reified T> SliderPreference(
  noinline title: @Composable (T) -> Unit,
  noinline summary: @Composable (T) -> Unit,
  v: T,
  crossinline onValueChange: (T) -> Unit,
  range: ClosedRange<T>,
) where T : Comparable<T>, T : Number {
  Preference(
    title,
    summary,
    v,
    enabled = false,
    bottomWidget = {
      Slider(
        valueRange = (range.start.toFloat())..(range.endInclusive.toFloat()),
        value = v.toFloat(),
        onValueChange = { onValueChange(it.toSpecificNumber()) }
      )
    }
  )
}