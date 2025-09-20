package com.example.flightplanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onChecklistsClick: () -> Unit,
    onTripsClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Flight Planner") }) }
    ) { inner ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = androidx.compose.ui.Alignment.CenterVertically)
        ) {
            Button(onClick = onChecklistsClick) {
                Text("Checklists")
            }
            Button(onClick = onTripsClick) {
                Text("Trips")
            }
        }
    }
}
