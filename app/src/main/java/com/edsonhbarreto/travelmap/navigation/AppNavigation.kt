package com.edsonhbarreto.travelmap.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.edsonhbarreto.travelmap.TripViewModel
import com.edsonhbarreto.travelmap.ui.screens.ChecklistScreen
import com.edsonhbarreto.travelmap.ui.screens.MapScreen
import com.edsonhbarreto.travelmap.ui.screens.PlaceDetailScreen
import com.edsonhbarreto.travelmap.ui.screens.PlacesScreen

private sealed class Tab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Map : Tab("map", "Mapa", Icons.Filled.Map)
    object Places : Tab("places", "Lugares", Icons.Filled.Photo)
    object Checklist : Tab("checklist", "Checklist", Icons.Filled.CheckCircle)
}

private const val PLACE_DETAIL_ROUTE = "place/{placeId}"

@Composable
fun AppNavigation(viewModel: TripViewModel) {
    val navController = rememberNavController()
    val places by viewModel.places.collectAsState()
    val checklistItems by viewModel.checklistItems.collectAsState()

    val tabs = listOf(Tab.Map, Tab.Places, Tab.Checklist)

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute?.hierarchy?.any { it.route == tab.route } == true,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Tab.Map.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Tab.Map.route) {
                MapScreen(
                    places = places,
                    onAddPlace = { name, description, lat, lng -> viewModel.addPlace(name, description, lat, lng) },
                    onMarkerClick = { place -> navController.navigate("place/${place.id}") }
                )
            }
            composable(Tab.Places.route) {
                PlacesScreen(
                    places = places,
                    onAddPlace = { name, description, lat, lng -> viewModel.addPlace(name, description, lat, lng) },
                    onPlaceClick = { place -> navController.navigate("place/${place.id}") }
                )
            }
            composable(Tab.Checklist.route) {
                ChecklistScreen(
                    items = checklistItems,
                    places = places,
                    onAdd = { title, notes, type, placeId -> viewModel.addChecklistItem(title, notes, type, placeId, null) },
                    onCheckedChange = { item, done -> viewModel.toggleChecklistItem(item, done) },
                    onDelete = { item -> viewModel.deleteChecklistItem(item) }
                )
            }
            composable(
                route = PLACE_DETAIL_ROUTE,
                arguments = listOf(navArgument("placeId") { type = androidx.navigation.NavType.LongType })
            ) { backStackEntry ->
                val placeId = backStackEntry.arguments?.getLong("placeId") ?: return@composable
                val place = places.firstOrNull { it.id == placeId } ?: return@composable
                PlaceDetailScreen(
                    place = place,
                    checklistItems = checklistItems.filter { it.placeId == placeId },
                    onBack = { navController.popBackStack() },
                    onImagesChanged = { uris -> viewModel.updatePlaceImages(place, uris) },
                    onAddChecklistItem = { title, notes, type, pId -> viewModel.addChecklistItem(title, notes, type, pId, null) },
                    onCheckedChange = { item, done -> viewModel.toggleChecklistItem(item, done) },
                    onDeleteChecklistItem = { item -> viewModel.deleteChecklistItem(item) }
                )
            }
        }
    }
}
