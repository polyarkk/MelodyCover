package com.skopzz.melodycover.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skopzz.melodycover.McContext
import com.skopzz.melodycover.cover.Cover
import com.skopzz.melodycover.dev.MockedContext
import com.skopzz.melodycover.ui.Main
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.preference
import me.zhanghai.compose.preference.preferenceCategory
import me.zhanghai.compose.preference.switchPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(ctx: McContext) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(onClick = { ctx.navController.navigate(Main) }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "nope",
                        )
                    }
                },
                title = {
                    Text("设置")
                }
            )
        }
    ) { innerPadding ->
        ProvidePreferenceLocals {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .widthIn(512.dp, 768.dp)
                ) {
                    preferenceCategory("position", { Text("位置") })
                    switchPreference(
                        key = "lock_vertical_position",
                        defaultValue = false,
                        title = { Text("锁定垂直位置") },
                    )
                    preference(
                        key = "reset_vertical_position",
                        title = { Text("重置垂直位置") },
                        summary = { Text("将上隐条置于顶部位置") },
                        onClick = {
                            Cover.instance?.setX(0)
                        }
                    )
                    switchPreference(
                        key = "lock_horizontal_position",
                        defaultValue = true,
                        title = { Text("锁定水平位置") },
                    )
                    preference(
                        key = "reset_horizontal_position",
                        title = { Text("重置水平位置") },
                        summary = { Text("将上隐条置于水平居中位置") },
                        onClick = {
                            Cover.instance?.setY(0)
                        }
                    )
                }
            }
        }
    }
}