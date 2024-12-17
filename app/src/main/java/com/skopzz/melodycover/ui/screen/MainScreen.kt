package com.skopzz.melodycover.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.skopzz.melodycover.R
import com.skopzz.melodycover.service.CoverService
import com.skopzz.melodycover.ui.ConfigRoute
import com.skopzz.melodycover.ui.SettingsRoute

@Composable
fun MainScreen(nav: NavController) {
  val ctx = LocalContext.current

  var permGranted by remember { mutableStateOf(canDrawOverlays(ctx)) }

  val permGrantLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
  ) {
    permGranted = canDrawOverlays(ctx)
  }

  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    Box(
      contentAlignment = Alignment.TopCenter,
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      Column(
        modifier = Modifier
          .widthIn(max = 512.dp)
          .padding(horizontal = 36.dp)
      ) {
        Text(
          ctx.getString(R.string.app_name),
          fontSize = 32.sp,
          modifier = Modifier.padding(start = 8.dp, top = 24.dp, bottom = 12.dp)
        )
        if (!permGranted) {
          ButtonCard(
            Icons.Default.Warning,
            "未授予悬浮窗权限！单击前往设置授予权限",
            bgColor = MaterialTheme.colorScheme.errorContainer,
            textColor = MaterialTheme.colorScheme.onErrorContainer,
            iconColor = MaterialTheme.colorScheme.onErrorContainer
          ) {
            val intent = Intent(
              ACTION_MANAGE_OVERLAY_PERMISSION,
              Uri.parse("package:${ctx.packageName}")
            )

            permGrantLauncher.launch(intent)
          }
        }
        EnabledCard(ctx)
        ButtonCard(Icons.AutoMirrored.Default.List, "配置") {
          nav.navigate(ConfigRoute)
        }
        ButtonCard(Icons.Default.Settings, "设置", true) {
          nav.navigate(SettingsRoute)
        }
        ButtonCard(Icons.Default.Info, "关于", true) {}
      }
    }
  }
}

@Composable
fun EnabledCard(ctx: Context) {
  val showing by CoverService.isRunning.collectAsState()

  var cardColor = if (showing) {
    MaterialTheme.colorScheme.primaryContainer
  } else {
    MaterialTheme.colorScheme.secondaryContainer
  }

  var textColor = if (showing) {
    MaterialTheme.colorScheme.onPrimaryContainer
  } else {
    MaterialTheme.colorScheme.onSecondaryContainer
  }

  ButtonCard(
    if (showing) Icons.Default.Check else Icons.Default.Clear,
    if (showing) "已启用" else "未启用",
    bgColor = cardColor,
    textColor = textColor,
    iconColor = textColor,
  ) {
    if (showing) {
      CoverService.stop(ctx)
    } else {
      CoverService.start(ctx)
    }
  }
}

@Composable
fun ButtonCard(
  icon: ImageVector,
  text: String,
  transparent: Boolean = false,
  bgColor: Color = CardDefaults.cardColors().containerColor,
  textColor: Color = Color.Companion.Unspecified,
  iconColor: Color = LocalContentColor.current,
  onClick: () -> Unit,
) {
  val defaultColors = CardDefaults.cardColors()

  val colors = CardColors(
    if (transparent) Color.Companion.Transparent else bgColor,
    defaultColors.contentColor,
    if (transparent) Color.Companion.Transparent else defaultColors.disabledContainerColor,
    defaultColors.disabledContentColor
  )

  val modifier = if (!transparent) {
    Modifier.Companion.size(width = 400.dp, height = 96.dp)
  } else {
    Modifier.Companion.size(width = 400.dp, height = 72.dp)
  }

  val elevation = if (!transparent) {
    CardDefaults.cardElevation(defaultElevation = 4.dp)
  } else {
    CardDefaults.cardElevation()
  }

  Card(
    onClick,
    modifier = modifier.padding(0.dp, 12.dp, 0.dp, 0.dp),
    colors = colors,
    elevation = elevation,
  ) {
    Row(
      verticalAlignment = Alignment.Companion.CenterVertically,
      modifier = Modifier.Companion
        .fillMaxSize()
        .padding(16.dp, 0.dp)
    ) {
      Icon(
        icon,
        tint = iconColor,
        contentDescription = null,
        modifier = Modifier.Companion
          .size(36.dp)
          .padding(0.dp, 0.dp, 8.dp, 0.dp)
      )
      Text(text, color = textColor)
    }
  }
}
