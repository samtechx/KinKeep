package app.kinkeep.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.People
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.kinkeep.R
import app.kinkeep.ui.dashboard.DashboardScreen
import app.kinkeep.ui.people.PeopleScreen
import app.kinkeep.ui.people.PersonDetailScreen
import app.kinkeep.ui.settings.SettingsScreen

sealed class Route(val route: String) {
    data object Dashboard : Route("dashboard")

    data object People : Route("people")

    data object Settings : Route("settings")

    data object PersonDetail : Route("person/{id}") {
        const val ARG_ID = "id"

        fun create(id: Long) = "person/$id"
    }
}

private data class TopLevelDestination(
    val route: Route,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
)

private val topLevelDestinations = listOf(
    TopLevelDestination(Route.Dashboard, R.string.nav_dashboard, Icons.Rounded.Dashboard),
    TopLevelDestination(Route.People, R.string.nav_people, Icons.Rounded.People),
    TopLevelDestination(Route.Settings, R.string.nav_settings, Icons.Rounded.Settings),
)

@Composable
fun KinKeepRoot() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val showBottomBar = topLevelDestinations.any { item ->
        currentDestination?.hierarchy?.any { it.route == item.route.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    topLevelDestinations.forEach { item ->
                        val label = stringResource(item.labelRes)
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == item.route.route } == true,
                            onClick = {
                                navController.navigate(item.route.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Dashboard.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Route.Dashboard.route) { DashboardScreen() }
            composable(Route.People.route) {
                PeopleScreen(onOpenPerson = { id -> navController.navigate(Route.PersonDetail.create(id)) })
            }
            composable(Route.Settings.route) { SettingsScreen() }
            composable(
                route = Route.PersonDetail.route,
                arguments = listOf(navArgument(Route.PersonDetail.ARG_ID) { type = NavType.LongType }),
            ) {
                PersonDetailScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
