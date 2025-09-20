package com.example.flightplanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightplanner.viewmodel.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    vm: TripViewModel,
    tripId: Long,
    onBack: () -> Unit
) {
    val trips = vm.trips.collectAsState().value
    val trip = trips.firstOrNull { it.id == tripId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(trip?.name ?: "Trip") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            if (trip == null) {
                Text("Trip not found")
            } else {
                if (trip.checklists.isEmpty()) {
                    Text("No checklists for this trip.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(trip.checklists) { cl ->
                            Column(Modifier.padding(8.dp)) {
                                Text(cl.name, style = MaterialTheme.typography.titleLarge)

                                cl.categories.forEach { cat ->
                                    Text(
                                        cat.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                    )

                                    cat.items.forEach { item ->
                                        Row(
                                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = item.checked,
                                                onCheckedChange = {
                                                    vm.toggleTripChecklistItem(trip.id, cl.id, cat.id, item.id)
                                                }
                                            )
                                            Text(item.text)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }
    }
}
