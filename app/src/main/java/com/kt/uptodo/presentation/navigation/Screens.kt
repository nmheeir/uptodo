package com.kt.uptodo.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kt.uptodo.R

sealed class Screens(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int,
    val route: String
) {
    data object Index : Screens(R.string.index, R.drawable.ic_home, "index")
    data object Calendar : Screens(R.string.Calendar, R.drawable.ic_calendar_month, "calendar")
    data object Focus : Screens(R.string.Focus, R.drawable.ic_schedule, "focus")

    data object More : Screens(R.string.more, R.drawable.ic_more_horiz, "more")

    companion object {
        val MainScreens = listOf(Index, Calendar, Focus, More)
    }
}