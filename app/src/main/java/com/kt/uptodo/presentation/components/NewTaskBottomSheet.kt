package com.kt.uptodo.presentation.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.kt.uptodo.R
import com.kt.uptodo.data.entities.TaskEntity
import com.kt.uptodo.presentation.components.dialog.CategoryDialog
import com.kt.uptodo.presentation.components.dialog.PriorityDialog
import com.kt.uptodo.presentation.components.dialog.TaskTimeDialog
import com.kt.uptodo.presentation.shared.NewTaskSheetUiAction
import com.kt.uptodo.utils.padding
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskBottomSheet(
    onDismiss: () -> Unit,
    action: (NewTaskSheetUiAction) -> Unit,
    newTask: TaskEntity
) {
    val focusManager = LocalFocusManager.current
    val (focus1, focus2) = remember { FocusRequester.createRefs() }

    var showTaskTime by remember { mutableStateOf(false) }
    var showTaskPriority by remember { mutableStateOf(false) }
    var showTaskCategory by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        focus1.requestFocus()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .padding(
                bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
            )

    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.mediumSmall),
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.padding.mediumSmall)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                        }
                    )
                }
        ) {
            Text(
                text = "Add task",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            val (title, onTitleChange) = remember { mutableStateOf(newTask.title) }
            OutlinedTextField(
                value = title,
                onValueChange = {
                    onTitleChange(it)
                    action(NewTaskSheetUiAction.UpdateNewTaskTitle(it))
                },
                label = {
                    Text(text = "Title")
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focus2.requestFocus() }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus1)
            )

            val (description, onDescriptionChange) = remember { mutableStateOf(newTask.description) }
            OutlinedTextField(
                value = description,
                onValueChange = {
                    onDescriptionChange(it)
                    action(NewTaskSheetUiAction.UpdateNewTaskDescription(it))
                },
                label = {
                    Text(text = "Description")
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus2)

            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
                ) {
                    //Time
                    IconButton(
                        onClick = { showTaskTime = true }
                    ) {
                        Icon(painterResource(R.drawable.ic_timer), null)
                    }

                    // Category
                    IconButton(
                        onClick = { showTaskCategory = true }
                    ) {
                        Icon(painterResource(R.drawable.ic_sell), null)
                    }

                    //Priority
                    IconButton(
                        onClick = { showTaskPriority = true }
                    ) {
                        Icon(painterResource(R.drawable.ic_flag_2), null)
                    }
                }

                IconButton(
                    onClick = {
                        action(NewTaskSheetUiAction.CreateNewTask)
                        onDismiss()
                    },
                    enabled = newTask.title.isNotEmpty()
                            && newTask.description.isNotEmpty()
                            && newTask.categoryId != 0L
                ) {
                    Icon(painterResource(R.drawable.ic_send), null)
                }
            }
        }
    }

    if (showTaskTime) {
        TaskTimeDialog(
            taskDateTime = newTask.deadline,
            onDismiss = { showTaskTime = false },
            onTaskDateTimeChange = {
                action(NewTaskSheetUiAction.UpdateNewTaskDeadline(it))
                showTaskCategory = false
            }
        )
    }

    if (showTaskPriority) {
        PriorityDialog(
            selectedPriority = newTask.priority,
            onValueSelected = {
                action(NewTaskSheetUiAction.UpdateNewTaskPriority(it))
                showTaskPriority = false
            },
            onDismiss = { showTaskPriority = false }
        )
    }

    if (showTaskCategory) {
        CategoryDialog(
            selectedCategoryId = newTask.categoryId,
            onValueSelected = {
                action(NewTaskSheetUiAction.UpdateNewTaskCategory(it.categoryId))
                showTaskCategory = false
            },
            onDismiss = { showTaskCategory = false }
        )
    }
}