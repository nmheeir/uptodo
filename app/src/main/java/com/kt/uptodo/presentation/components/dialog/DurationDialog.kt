package com.kt.uptodo.presentation.components.dialog

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kt.uptodo.R
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.presentation.components.SliderItem
import com.kt.uptodo.presentation.theme.UpTodoTheme
import timber.log.Timber

@Composable
fun DurationDialog(
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    var sliderValue by remember { mutableIntStateOf(10) }
    DefaultDialog(
        onDismiss = onDismiss,
        buttons = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel))
            }
            TextButton(
                onClick = {
                    onConfirm(sliderValue.toLong())
                }
            ) {
                Text(text = stringResource(R.string.ok))
            }
        }
    ) {
        SliderItem(
            label = stringResource(R.string.action_cancel),
            value = sliderValue,
            valueText = "aofdj",
            onChange = {
                sliderValue = it
                Timber.d(sliderValue.toString())
            },
            max = 99,
            min = 0,
            steps = 11,
        )
    }
}