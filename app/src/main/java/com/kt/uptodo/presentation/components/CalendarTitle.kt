package com.kt.uptodo.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kt.uptodo.R
import com.kt.uptodo.extensions.displayText
import java.time.YearMonth

@Composable
fun CalendarTitle(
    modifier: Modifier = Modifier,
    currentMonth: YearMonth,
    isHorizontal: Boolean = true,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalendarNavigationIcon(
            icon = R.drawable.ic_keyboard_arrow_left,
            contentDescription = "Previous",
            onClick = goToPrevious,
            isHorizontal = isHorizontal,
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .testTag("MonthTitle"),
            text = currentMonth.displayText(),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
        )
        CalendarNavigationIcon(
            icon = R.drawable.ic_keyboard_arrow_right,
            contentDescription = "Next",
            onClick = goToNext,
            isHorizontal = isHorizontal,
        )
    }
}

@Composable
private fun CalendarNavigationIcon(
    @DrawableRes icon: Int,
    contentDescription: String? = null,
    isHorizontal: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .clip(CircleShape)
            .clickable(role = Role.Button, onClick = onClick)
    ) {
        val rotation by animateFloatAsState(
            targetValue = if (isHorizontal) 0f else 90f,
            label = "CalendarNavigationIconAnimation"
        )

        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .align(Alignment.Center)
                .rotate(rotation),
        )
    }
}