package com.kt.uptodo.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.kt.uptodo.utils.padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(
    modifier: Modifier = Modifier,
    dismissState: SwipeToDismissBoxState,
    @DrawableRes startIcon: Int? = null,
    @DrawableRes endIcon: Int? = null
) {

    val targetValue = dismissState.targetValue

    val color = when (targetValue) {
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.primaryContainer
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.tertiaryContainer
        SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.secondaryContainer
    }

    val contentColor = when (targetValue) {
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.onPrimaryContainer
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.onTertiaryContainer
        SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .fillMaxSize()
            .background(color)
    ) {
        startIcon?.let {
            Icon(
                painter = painterResource(startIcon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
        endIcon?.let {
            Icon(
                painter = painterResource(endIcon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = MaterialTheme.padding.mediumSmall)
            )
        }
    }
}