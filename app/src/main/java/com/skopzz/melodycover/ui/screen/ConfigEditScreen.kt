package com.skopzz.melodycover.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skopzz.melodycover.McContext
import com.skopzz.melodycover.cover.CoverType
import com.skopzz.melodycover.util.getResourceBitmapSize
import com.skopzz.melodycover.util.getFileBitmapSize
import com.skopzz.melodycover.util.roundTo
import com.skopzz.melodycover.ui.component.preference.ColorPreference
import com.skopzz.melodycover.ui.component.CoverPreview
import com.skopzz.melodycover.ui.component.preference.BasePreference
import com.skopzz.melodycover.ui.component.preference.EnumPreference
import com.skopzz.melodycover.ui.component.preference.SliderPreference
import com.skopzz.melodycover.util.copyFileFromUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigEditScreen(ctx: McContext, key: String, vm: ConfigEditViewModel = viewModel()) {
  val uiState by vm.uiState.collectAsState()

  val scrollState = rememberScrollState()

  val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
    vm.updateConfiguration {
      if (uri == null) {
        return@updateConfiguration it
      }

      val file = ctx.context.copyFileFromUri(uri, "bar.coverImg")

      // change imageUpdateMs for a force update
      it.copy(imagePath = file.path, imageUpdateMs = System.currentTimeMillis())
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        colors = topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
          IconButton(onClick = {
            ctx.navController.navigateUp()
          }) {
            Icon(
              Icons.AutoMirrored.Default.ArrowBack,
              contentDescription = "nope",
            )
          }
        },
        title = { Text("配置：%s".format(uiState.conf.name)) },
        actions = {
          TextButton(onClick = {

          }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                Icons.Default.Check,
                contentDescription = "nope",
              )
              Text("应用当前配置")
            }
          }
        }
      )
    }
  ) { innerPadding ->
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
    ) {
      Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        contentAlignment = Alignment.Center,
      ) {
        CoverPreview(uiState.conf)
      }
      Column(
        modifier = Modifier
          .widthIn(max = 800.dp)
          .verticalScroll(scrollState),
      ) {
        EnumPreference(
          title = { Text("上隐条类型") },
          summary = {
            Text(it)
          },
          v = uiState.conf.coverType,
          onValueChange = { v ->
            vm.updateConfiguration {
              it.copy(coverType = v)
            }
          },
          values = listOf(CoverType.COLOR, CoverType.IMAGE),
          displays = listOf("纯色", "图片"),
        )
        when (uiState.conf.coverType) {
          CoverType.COLOR -> {
            ColorPreference(
              title = { Text("上隐条颜色") },
              summary = {
                Text("#${it.toString(16)}")
              },
              v = uiState.conf.color,
              onValueChange = { v ->
                vm.updateConfiguration {
                  it.copy(color = v)
                }
              }
            )
            SliderPreference(
              title = { Text("宽度") },
              summary = {
                Text("$it")
              },
              v = uiState.conf.width,
              onValueChange = { v ->
                vm.updateConfiguration {
                  it.copy(width = v)
                }
              },
              range = 0..1000
            )
            SliderPreference(
              title = { Text("高度") },
              summary = {
                Text("$it")
              },
              v = uiState.conf.height,
              onValueChange = { v ->
                vm.updateConfiguration {
                  it.copy(height = v)
                }
              },
              range = 0..1000
            )
          }
          CoverType.IMAGE -> {
            val maxSize by remember {
              derivedStateOf {
                if (uiState.conf.imagePath != null) {
                  getFileBitmapSize(uiState.conf.imagePath!!)
                } else {
                  ctx.context.getResourceBitmapSize(uiState.conf.imageId)
                }
              }
            }

            BasePreference(
              title = { Text("图片路径") },
              summary = {
                Text("$it")
              },
              v = uiState.conf.imagePath ?: uiState.conf.imageId,
              onClick = {
                pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
              }
            )
            SliderPreference(
              title = { Text("缩放倍率") },
              summary = {
                Text("$it")
              },
              v = uiState.conf.imageScale,
              onValueChange = { v ->
                vm.updateConfiguration {
                  it.copy(imageScale = v.roundTo(2))
                }
              },
              range = 0f..10f
            )
            SliderPreference(
              title = { Text("左侧隐藏宽度") },
              summary = {
                Text("$it")
              },
              v = uiState.conf.imageInsetL,
              onValueChange = { v ->
                vm.updateConfiguration {
                  it.copy(imageInsetL = v)
                }
              },
              range = 0..(maxSize.first - uiState.conf.imageInsetR)
            )
            SliderPreference(
              title = { Text("右侧隐藏宽度") },
              summary = {
                Text("$it")
              },
              v = uiState.conf.imageInsetR,
              onValueChange = { v ->
                vm.updateConfiguration {
                  it.copy(imageInsetR = v)
                }
              },
              range = 0..(maxSize.first - uiState.conf.imageInsetL)
            )
            SliderPreference(
              title = { Text("顶部隐藏高度") },
              summary = {
                Text("$it")
              },
              v = uiState.conf.imageInsetT,
              onValueChange = { v ->
                vm.updateConfiguration {
                  it.copy(imageInsetT = v)
                }
              },
              range = 0..(maxSize.second - uiState.conf.imageInsetB)
            )
            SliderPreference(
              title = { Text("底部隐藏高度") },
              summary = {
                Text("$it")
              },
              v = uiState.conf.imageInsetB,
              onValueChange = { v ->
                vm.updateConfiguration {
                  it.copy(imageInsetB = v)
                }
              },
              range = 0..(maxSize.second - uiState.conf.imageInsetT)
            )
          }
        }
      }
    }
  }
}