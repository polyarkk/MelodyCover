package com.skopzz.melodycover.ui.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.skopzz.melodycover.util.fromHex
import com.skopzz.melodycover.util.fromHexString
import com.skopzz.melodycover.util.isValidArgbHexString
import com.skopzz.melodycover.util.toHex
import com.skopzz.melodycover.util.toHexString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerDialog(
  defaultValue: ULong = 0xff6aa6ffu,
  onDismissRequest: () -> Unit,
  onConfirmRequest: (ULong) -> Unit
) {
  val controller = rememberColorPickerController()

  var text by remember { mutableStateOf("") }

  BasicAlertDialog(
    onDismissRequest = onDismissRequest
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      shape = RoundedCornerShape(16.dp),
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .width(IntrinsicSize.Max)
          .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = "选取颜色",
          modifier = Modifier
            .fillMaxWidth(),
          textAlign = TextAlign.Center,
          fontSize = 24.sp
        )
        HsvColorPicker(
          modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(10.dp),
          controller = controller,
          initialColor = Color.fromHex(defaultValue),
          onColorChanged = { e: ColorEnvelope ->
            text = e.color.toHexString()
          }
        )
        AlphaSlider(
          modifier = Modifier
            .width(280.dp)
            .padding(10.dp)
            .height(35.dp),
          controller = controller,
        )
        BrightnessSlider(
          modifier = Modifier
            .width(280.dp)
            .padding(10.dp)
            .height(35.dp),
          controller = controller,
        )
        AlphaTile(
          modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(6.dp)),
          controller = controller
        )
        TextField(
          value = text,
          onValueChange = {
            text = it

            if (isValidArgbHexString(text)) {
              controller.selectByColor(Color.fromHexString(text), true)
            }
          },
          textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
          ),
          colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = controller.selectedColor.value,
            unfocusedTextColor = controller.selectedColor.value,
          )
        )
        Row {
          Spacer(Modifier.weight(0.3f))
          TextButton(
            onClick = {
              onDismissRequest()
            },
          ) {
            Text("取消")
          }
          TextButton(
            onClick = {
              onConfirmRequest(controller.selectedColor.value.toHex())
              onDismissRequest()
            },
          ) {
            Text("确定")
          }
        }
      }
    }
  }
}