package com.example.flightplanner.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightplanner.viewmodel.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(
    vm: TripViewModel,
    onBack: () -> Unit,
    onOpenTrip: (Long) -> Unit
) {
    val trips by vm.trips.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newTripName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trips") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Trip")
            }
        }
    ) { inner ->
        if (trips.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                verticalArrangement = Arrangement.Center
            ) {
                Text("No trips yet. Add one with +")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(trips) { trip ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenTrip(trip.id) }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(trip.name, style = MaterialTheme.typography.titleMedium)
                        IconButton(onClick = { vm.deleteTrip(trip.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Trip")
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("New Trip") },
            text = {
                OutlinedTextField(
                    value = newTripName,
                    onValueChange = { newTripName = it },
                    label = { Text("Trip name") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newTripName.isNotBlank()) {
                        vm.addTrip(newTripName.trim())
                        newTripName = ""
                        showDialog = false
                    }
                }) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}
