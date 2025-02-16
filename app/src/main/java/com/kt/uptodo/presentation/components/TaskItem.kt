package com.kt.uptodo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kt.uptodo.R
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.data.enums.Priority
import com.kt.uptodo.data.relations.TaskDetail
import com.kt.uptodo.extensions.parseMinute
import com.kt.uptodo.utils.Gap
import com.kt.uptodo.utils.padding
import java.time.Duration
import java.time.OffsetDateTime

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    taskDetail: TaskDetail,
    onClick: () -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }

    val now = OffsetDateTime.now()
    val endDate = taskDetail.task.end

    val deadlineText = if (now.dayOfMonth == endDate.dayOfMonth) {
        "Today at " + endDate.parseMinute()
    } else {
        val duration = Duration.between(now, endDate)
        val minutesLeft = duration.toMinutes()
        val hoursLeft = duration.toHours()
        val daysLeft = duration.toDays()

        when {
            minutesLeft < 60 -> "Due in $minutesLeft minutes"
            hoursLeft < 24 -> "Due in $hoursLeft hours"
            daysLeft in 1..6 -> {
                val remainingHours = hoursLeft % 24
                "Due in $daysLeft days, $remainingHours hours"
            }

            else -> "Due in $daysLeft days"
        }
    }

    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable {}
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxWidth()
                .padding(MaterialTheme.padding.small)
        ) {
//            Gap(width = MaterialTheme.padding.medium)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = taskDetail.task.title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = deadlineText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        CategoryItem(category = taskDetail.category)
                        Gap(width = MaterialTheme.padding.small)
                        PriorityItem(priority = taskDetail.task.priority)
                    }
                }
            }

        }
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: CategoryEntity
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(Color(android.graphics.Color.parseColor(category.color)))
    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(MaterialTheme.padding.small)
        )
    }
}

@Composable
private fun PriorityItem(modifier: Modifier = Modifier, priority: Priority) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                MaterialTheme.shapes.extraSmall
            )
            .padding(MaterialTheme.padding.extraSmall)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_flag_2),
            contentDescription = null
        )
        Text(
            text = priority.name,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
