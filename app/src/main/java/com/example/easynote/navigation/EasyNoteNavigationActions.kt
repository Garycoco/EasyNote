package com.example.easynote.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.datamate.easynote.R

object EasyNoteRoute {
    const val home = "Home"
    const val favourites = "Favourites"
}
data class EasyNoteTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)
class EasyNoteNavigationActions(private val navController: NavController) {
    fun navigateTo(destination: EasyNoteTopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
val TOP_LEVEL_DESTINATIONS = listOf(
    EasyNoteTopLevelDestination(
        route = EasyNoteRoute.home,
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.home
    ),
    EasyNoteTopLevelDestination(
        route = EasyNoteRoute.favourites,
        selectedIcon = Icons.Rounded.Favorite,
        unselectedIcon = Icons.Rounded.FavoriteBorder,
        iconTextId = R.string.home
    )
)