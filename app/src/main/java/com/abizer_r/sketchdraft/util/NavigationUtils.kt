package com.abizer_r.sketchdraft.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

object NavigationUtils {
}

/**
 * Took from PL video: https://www.youtube.com/watch?v=h61Wqy3qcKg&ab_channel=PhilippLackner
 */
@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}