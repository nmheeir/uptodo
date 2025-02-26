package com.kt.uptodo.presentation.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kt.uptodo.utils.clickable
import java.time.LocalDate

@Composable
fun Day(
    day: LocalDate,
    isSelected: Boolean,
    isSelectable: Boolean,
    onClick: (LocalDate) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(6.dp)
            .clip(CircleShape)
            .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(
                enabled = isSelected,
                showRipple = !isSelected,
                onClick = { onClick(day) },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f) // This is important for square-sizing!
                .padding(6.dp)
                .clip(CircleShape)
                .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                .clickable(
                    enabled = isSelectable,
                    showRipple = !isSelected,
                    onClick = { onClick(day) },
                ),
            contentAlignment = Alignment.Center,
        ) {
            val textColor = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                isSelectable -> Color.Unspecified
                else -> MaterialTheme.colorScheme.outlineVariant
            }
            Text(
                text = day.dayOfMonth.toString(),
                color = textColor,
                fontSize = 14.sp,
            )
        }
    }
}