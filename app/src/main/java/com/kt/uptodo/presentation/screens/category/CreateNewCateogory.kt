package com.kt.uptodo.presentation.screens.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kt.uptodo.R
import com.kt.uptodo.utils.DefaultCategoryColors
import com.kt.uptodo.utils.padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewCategoryScreen() {
    val lazyRowState = rememberLazyListState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.action_create_new_category),
                    )
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            Text(
                text = stringResource(R.string.label_category_name),
                style = MaterialTheme.typography.titleLarge
            )

            val (categoryName, onCategoryNameChange) = remember { mutableStateOf("") }
            OutlinedTextField(
                value = categoryName,
                onValueChange = onCategoryNameChange,
                shape = MaterialTheme.shapes.small,
                placeholder = {
                    Text(
                        text = "Eg: Work"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = stringResource(R.string.label_category_name),
                style = MaterialTheme.typography.labelMedium
            )

            var selectedColor by remember { mutableStateOf(DefaultCategoryColors[1]) }
            LazyRow(
                state = lazyRowState,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
            ) {
                items(
                    items = DefaultCategoryColors,
                    key = { it.hashCode() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(it)
                            .clickable { selectedColor = it }
                    ) {
                        if (selectedColor == it) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}