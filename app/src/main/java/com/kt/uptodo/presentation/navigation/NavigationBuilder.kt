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
import com.kt.uptodo.presentation.screens.category.CreateNewCategoryScreen

fun NavGraphBuilder.navigationBuilder(
    navController: NavHostController
) {
    composable(route = Screens.Index.route) {
        IndexScreen(navController)
    }

    composable(
        route = Screens.Calendar.route
    ) {
        CalendarScreen()
    }

    composable(
        route = Screens.Focus.route
    ) {
        FocusScreen()
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

    composable(
        route = "create_new_category"
    ) {
        CreateNewCategoryScreen()
    }
}