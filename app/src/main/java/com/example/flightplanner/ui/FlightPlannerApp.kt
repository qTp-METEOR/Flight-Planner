package com.example.flightplanner.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flightplanner.data.InMemoryChecklistRepository
import com.example.flightplanner.data.InMemoryTripRepository
import com.example.flightplanner.viewmodel.ChecklistViewModel
import com.example.flightplanner.viewmodel.ChecklistViewModelFactory
import com.example.flightplanner.viewmodel.TripViewModel
import com.example.flightplanner.viewmodel.TripViewModelFactory

@Composable
fun FlightPlannerApp() {
    val navController = rememberNavController()

    val checklistRepo = InMemoryChecklistRepository()
    val checklistVm: ChecklistViewModel = viewModel(factory = ChecklistViewModelFactory(checklistRepo))
    val tripRepo = InMemoryTripRepository()
    val tripVm: TripViewModel = viewModel(factory = TripViewModelFactory(tripRepo))


    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MainMenuScreen(
                onChecklistsClick = { navController.navigate("checklists") },
                onTripsClick = { navController.navigate("trips") }
            )
        }
        composable("checklists") {
            ChecklistTemplatesScreen(
                vm = checklistVm,
                onBack = { navController.popBackStack() },
                onOpenTemplate = { id -> navController.navigate("checklist/$id") }
            )
        }
        composable(
            "checklist/{templateId}",
            arguments = listOf(navArgument("templateId") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("templateId") ?: return@composable
            ChecklistTemplateDetailScreen(
                vm = checklistVm,
                templateId = id,
                onBack = { navController.popBackStack() }
            )
        }
        composable("trips") {
            TripsScreen(
                onBack = { navController.popBackStack() },
                checklistVm = checklistVm,
                vm = tripVm,
                onOpenTrip = { id -> navController.navigate("trip/$id") }
            )
        }
        composable(
            route = "trip/{tripId}",
            arguments = listOf(navArgument("tripId") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("tripId") ?: return@composable
            TripDetailScreen(
                vm = tripVm,
                tripId = id,
                onBack = { navController.popBackStack() }
            )
        }

    }
}
