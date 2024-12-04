package com.skopzz.melodycover.ui.screen

import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skopzz.melodycover.McContext
import com.skopzz.melodycover.R
import com.skopzz.melodycover.ui.ConfigEditRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
  ctx: McContext
) {
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
        // Add 5 items
        items(5) { index ->
          ConfigRow(ctx, "", "Item: $index", index == 2)
        }
      }
    }
  }
}

@Composable
fun ConfigRow(ctx: McContext, key: String, name: String, selected: Boolean) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .height(IntrinsicSize.Min)
      .padding(vertical = 6.dp)
  ) {
    var expanded by remember { mutableStateOf(false) }

    RadioButton(selected, onClick = null, modifier = Modifier.padding(horizontal = 8.dp))
    Text(name)
    Spacer(modifier = Modifier.weight(1.0f))
    VerticalDivider(modifier = Modifier.padding(end = 4.dp))
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
            ctx.navController.navigate(ConfigEditRoute("default"))
          }
        )
        DropdownMenuItem(
          leadingIcon = { Icon(painterResource(R.drawable.content_copy), null) },
          text = { Text("复制") },
          onClick = {
            // todo
          }
        )
        DropdownMenuItem(
          leadingIcon = { Icon(Icons.Default.Delete, null, tint = Color.Red) },
          text = { Text("删除", color = Color.Red) },
          onClick = {
            expanded = false
            AlertDialog.Builder(ctx.context)
              .setTitle("删除配置")
              .setMessage("是否删除上隐条配置 -- $name？")
              .setPositiveButton("删除", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(ctx.context, "todo deleted", Toast.LENGTH_SHORT)
                  .show()
              })
              .setNegativeButton("取消", null)
              .show()
          }
        )
      }
    }
  }
}
