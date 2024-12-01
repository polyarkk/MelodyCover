package com.skopzz.melodycover.ui.screen

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skopzz.melodycover.McContext
import com.skopzz.melodycover.service.CoverService
import com.skopzz.melodycover.ui.Config
import com.skopzz.melodycover.ui.Settings

@Composable
fun MainScreen(ctx: McContext) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                Text(
                    "MelodyCover",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(8.dp, 24.dp, 0.dp, 8.dp)
                )
                EnabledCard(ctx.context)
                ButtonCard(Icons.AutoMirrored.Default.List, "配置") {
                    ctx.navController.navigate(Config)
                }
                ButtonCard(Icons.Default.Settings, "设置", true) {
                    ctx.navController.navigate(Settings)
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
        if (isSystemInDarkTheme()) {
            Color(0xff6a6aff)
        } else {
            Color(0xFF1D4F9E)
        }
    } else {
        Color.Companion.Gray
    }

    ButtonCard(
        if (showing) Icons.Default.Check else Icons.Default.Clear,
        if (showing) "已启用" else "未启用",
        bgColor = cardColor,
        textColor = Color.Companion.White,
        iconColor = Color.Companion.White,
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
    onClick: () -> Unit
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
