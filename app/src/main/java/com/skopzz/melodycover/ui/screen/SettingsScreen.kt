package com.skopzz.melodycover.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skopzz.melodycover.service.CoverService
import com.skopzz.melodycover.ui.component.preference.Preference
import com.skopzz.melodycover.ui.component.preference.PreferenceCategory
import com.skopzz.melodycover.ui.component.preference.SwitchPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(nav: NavController) {
  val ctx = LocalContext.current
  val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory(ctx))
  val uiState by vm.uiState.collectAsState()
  val scrollState = rememberScrollState()

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        colors = topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
          IconButton(onClick = { nav.navigateUp() }) {
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
    Box(
      contentAlignment = Alignment.TopCenter,
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    ) {
      Column(
        modifier = Modifier
          .widthIn(max = 800.dp)
          .verticalScroll(scrollState),
      ) {
        PreferenceCategory("位置")
        SwitchPreference(
          title = { Text("锁定水平位置") },
          v = uiState.lockHorizontalPosition,
          onValueChange = { vm.set("lock_horizontal_position", it) }
        )
        Preference(
          title = { Text("重置水平位置") },
          summary = { Text("将上隐条置于水平居中位置") },
          v = null,
          onClick = {
            CoverService.sendIo(ctx, "setX", 0)
          }
        )
        SwitchPreference(
          title = { Text("锁定垂直位置") },
          v = uiState.lockVerticalPosition,
          onValueChange = { vm.set("lock_vertical_position", it) }
        )
        Preference(
          title = { Text("重置垂直位置") },
          summary = { Text("将上隐条置于顶部位置") },
          v = null,
          onClick = {
            CoverService.sendIo(ctx, "setY", 0)
          }
        )
        PreferenceCategory("操作")
        SwitchPreference(
          title = { Text("连续三次点按关闭上隐条") },
          v = uiState.gestureClose,
          onValueChange = { vm.set("gesture_close", it) }
        )
      }
    }
  }
}