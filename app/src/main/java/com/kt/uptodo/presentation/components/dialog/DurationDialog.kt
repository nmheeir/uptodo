package com.kt.uptodo.presentation.components.dialog

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kt.uptodo.R
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.presentation.components.SliderItem
import com.kt.uptodo.presentation.theme.UpTodoTheme

@Composable
fun DurationDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    DefaultDialog(
        onDismiss = onDismiss,
        buttons = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel))
            }
        }
    ) {
        SliderItem(
            label = stringResource(R.string.action_cancel),
            value = 0,
            valueText = "aofdj",
            onChange = {

            },
            max = 10,
            min = 0,
            steps = 0
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Test() {
    UpTodoTheme {
        DurationDialog(onDismiss = {})
    }
}