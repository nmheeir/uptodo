package com.kt.uptodo.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import com.kt.uptodo.utils.padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderItem(
    label: String,
    value: Int,
    valueText: String,
    onChange: (Int) -> Unit,
    max: Int,
    min: Int = 0,
    steps: Int = 0,
    labelStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    pillColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.padding.large,
                vertical = MaterialTheme.padding.mediumSmall,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        ) {
            Text(
                text = label,
                style = labelStyle,
                modifier = Modifier.weight(1f),
            )
            Pill(
                text = valueText,
                style = MaterialTheme.typography.bodyMedium,
                color = pillColor,
            )
        }
        Slider(
            value = value,
            onValueChange = f@{
                if (it == value) return@f
                onChange(it)
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            },
            valueRange = min..max,
            steps = steps,
        )
    }
}