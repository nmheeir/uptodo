package com.kt.uptodo.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kt.uptodo.presentation.screens.CalendarScreen
import com.kt.uptodo.presentation.screens.FocusScreen
import com.kt.uptodo.presentation.screens.index.IndexScreen
import com.kt.uptodo.presentation.screens.MoreScreen
import com.kt.uptodo.presentation.screens.TaskDetailScreen

fun NavGraphBuilder.navigationBuilder(
    navController: NavHostController
) {
    composable(route = Screens.Index.route) {
        IndexScreen(navController)
    }

    composable(
        route = Screens.Calendar.route
    ) {
        CalendarScreen(navController)
    }

    composable(
        route = Screens.Focus.route,
        arguments = listOf(
            navArgument("duration") {
                type = NavType.LongType
                defaultValue = 0L
            }
        )
    ) {
        FocusScreen(navController)
    }

    composable(
        route = Screens.More.route
    ) {
        MoreScreen()
    }

    composable(
        route = "task_detail/{taskId}",
        arguments = listOf(
            navArgument("taskId") {
                type = NavType.LongType
            }
        )
    ) {
        TaskDetailScreen(navController)
    }
}