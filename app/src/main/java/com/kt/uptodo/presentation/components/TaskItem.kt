package com.kt.uptodo.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kt.uptodo.R
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.data.relations.TaskDetail
import com.kt.uptodo.presentation.theme.UpTodoTheme
import com.kt.uptodo.utils.Gap
import com.kt.uptodo.utils.fakeTasks
import com.kt.uptodo.utils.padding

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    taskDetail: TaskDetail
) {
    var isChecked by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.padding.small)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = !isChecked
                }
            )
            Gap(width = MaterialTheme.padding.medium)

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = taskDetail.task.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    CategoryItem(category = taskDetail.category)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                MaterialTheme.shapes.medium
                            )
                    ) {
                        Box(
                            modifier = Modifier.padding(MaterialTheme.padding.extraSmall)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_flag_2),
                                contentDescription = null
                            )
                            Text(
                                text = taskDetail.task.priority.name,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Test() {
    UpTodoTheme {
        TaskItem(
            taskDetail = TaskDetail(
                task = fakeTasks[1],
                category = CategoryEntity(name = "Học")
            )
        )
    }
}
