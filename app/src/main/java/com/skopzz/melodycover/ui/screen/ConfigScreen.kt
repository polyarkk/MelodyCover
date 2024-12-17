package com.skopzz.melodycover.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skopzz.melodycover.R
import com.skopzz.melodycover.ui.ConfigEditRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(nav: NavController) {
  val ctx = LocalContext.current

  val vm = viewModel(factory = ConfigViewModel.Factory(ctx)) as ConfigViewModel
  val uiState by vm.uiState.collectAsState()

  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  BackHandler {
    vm.deleteConfiguration()
    nav.navigateUp()
  }

  Scaffold(
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        colors = topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
          IconButton(onClick = {
            nav.navigateUp()
          }) {
            Icon(
              Icons.AutoMirrored.Default.ArrowBack,
              contentDescription = "nope",
            )
          }
        },
        title = {
          Text("配置")
        }
      )
    }
  ) { innerPadding ->
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
        items(uiState.configs.toList()) {
          ConfigRow(
            it.first,
            it.second,
            uiState.loadedConfigKey == it.first,
            onEdit = { k ->
              nav.navigate(ConfigEditRoute(k))
            },
            onCopy = { k ->
              nav.navigate(ConfigEditRoute(vm.copyConfiguration(k)))
            },
            onDelete = { k ->
              vm.showDeleteDialog(it)
            },
            onSelect = { k ->
              vm.selectConfiguration(ctx, k)
            }
          )
        }
      }
    }
  }

  if (uiState.deleting != null) {
    AlertDialog(
      icon = {
        Icon(Icons.Default.Warning, null)
      },
      title = {
        Text("删除配置")
      },
      text = {
        Text("是否删除上隐条配置： ${uiState.deleting?.second}？")
      },
      onDismissRequest = {
        vm.dismissDeleteDialog()
      },
      confirmButton = {
        TextButton(onClick = {
          val deleting = uiState.deleting!!

          // for uncompleted delete request
          vm.deleteConfiguration()

          scope.launch {
            vm.scheduleDeleteConfiguration(deleting.first)

            snackbarHostState.showSnackbar(
              "配置 [${deleting.second}] 已删除",
              actionLabel = "撤销",
              duration = SnackbarDuration.Short,
            ).run {
              when (this) {
                SnackbarResult.Dismissed -> vm.deleteConfiguration()
                SnackbarResult.ActionPerformed -> vm.undoDeleteConfiguration()
              }
            }
          }

          vm.dismissDeleteDialog()
        }) {
          Text("删除")
        }
      },
      dismissButton = {
        TextButton(onClick = { vm.dismissDeleteDialog() }) {
          Text("取消")
        }
      }
    )
  }
}

@Composable
fun ConfigRow(
  key: String,
  name: String,
  selected: Boolean,
  onEdit: (String) -> Unit,
  onCopy: (String) -> Unit,
  onDelete: (String) -> Unit,
  onSelect: (String) -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .height(IntrinsicSize.Min)
      .padding(vertical = 6.dp)
      .clickable(true) { onSelect(key) }
  ) {
    var expanded by remember { mutableStateOf(false) }

    RadioButton(selected, onClick = null, modifier = Modifier.padding(horizontal = 8.dp))
    Text(name)
    Spacer(modifier = Modifier.weight(1.0f))
    VerticalDivider(modifier = Modifier.padding(end = 4.dp))
    if (key == "default") {
      IconButton(onClick = { onCopy(key) }) {
        Icon(painterResource(R.drawable.content_copy), null)
      }
    } else {
      IconButton(onClick = { expanded = true }) {
        Icon(Icons.Default.MoreVert, null)
        DropdownMenu(
          expanded = expanded,
          onDismissRequest = { expanded = false }
        ) {
          DropdownMenuItem(
            leadingIcon = { Icon(Icons.Default.Edit, null) },
            text = { Text("编辑") },
            onClick = {
              expanded = false
              onEdit(key)
            }
          )
          DropdownMenuItem(
            leadingIcon = { Icon(painterResource(R.drawable.content_copy), null) },
            text = { Text("复制") },
            onClick = {
              expanded = false
              onCopy(key)
            }
          )
          DropdownMenuItem(
            leadingIcon = { Icon(Icons.Default.Delete, null) },
            text = { Text("删除") },
            colors = MenuDefaults.itemColors().copy(
              leadingIconColor = Color.Red,
              textColor = Color.Red,
            ),
            enabled = key != "default",
            onClick = {
              expanded = false
              onDelete(key)
            }
          )
        }
      }
    }
  }
}
