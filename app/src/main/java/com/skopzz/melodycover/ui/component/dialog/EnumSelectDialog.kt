package com.skopzz.melodycover.ui.component.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <T : Enum<T>> EnumSelectDialog(
    crossinline title: @Composable (T) -> Unit,
    defaultValue: T,
    values: List<T>,
    displays: List<String>,
    crossinline onValueChange: (T) -> Unit,
    noinline onDismissRequest: () -> Unit,
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            LazyColumn(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                item("t") {
                    ProvideTextStyle(
                        value = MaterialTheme.typography.titleMedium,
                        content = { title(defaultValue) }
                    )
                }
                items(values.size) {
                    val selected = values[it] == defaultValue

                    Row(
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .selectable(
                                selected = selected,
                                enabled = true,
                                role = Role.Companion.RadioButton,
                                onClick = {
                                    onValueChange(values[it])
                                    onDismissRequest()
                                }
                            ),
                    ) {
                        RadioButton(selected, onClick = { })
                        Text(displays[it])
                    }
                }
                item("c") {
                    Row {
                        Spacer(Modifier.Companion.weight(0.3f))
                        TextButton(
                            onClick = {
                                onDismissRequest()
                            },
                        ) {
                            Text("取消")
                        }
                    }
                }
            }
        }
    }
}