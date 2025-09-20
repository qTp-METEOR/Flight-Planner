package com.example.flightplanner.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightplanner.viewmodel.TripViewModel

// ----------------- Trip Detail (mini menu) -----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    vm: TripViewModel,
    tripId: Long,
    onBack: () -> Unit,
    onOpenChecklists: (Long) -> Unit,
    onOpenPlan: (Long) -> Unit
) {
    val trips = vm.trips.collectAsState().value
    val trip = trips.firstOrNull { it.id == tripId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(trip?.name ?: "Trip") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (trip == null) {
                Text("Trip not found")
            } else {
                Button(
                    onClick = { onOpenChecklists(trip.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Checklists")
                }
                Button(
                    onClick = { onOpenPlan(trip.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Plan")
                }
            }
        }
    }
}

// ----------------- Trip Checklists -----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripChecklistsScreen(
    vm: TripViewModel,
    tripId: Long,
    onBack: () -> Unit
) {
    val trips = vm.trips.collectAsState().value
    val trip = trips.firstOrNull { it.id == tripId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip Checklists") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { inner ->
        if (trip == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                contentAlignment = Alignment.Center
            ) {
                Text("Trip not found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(8.dp)
            ) {
                trip.checklists.forEach { checklist ->
                    item {
                        Text(
                            text = checklist.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    checklist.categories.forEach { category ->
                        item {
                            Text(
                                text = "• ${category.name}",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                            )
                        }
                        items(category.items, key = { it.id }) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 32.dp, top = 2.dp, bottom = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = item.checked,
                                    onCheckedChange = {
                                        vm.toggleTripChecklistItem(
                                            tripId = trip.id,
                                            checklistId = checklist.id,
                                            categoryId = category.id,
                                            itemId = item.id
                                        )
                                    }
                                )
                                Text(text = item.text, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                    item {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = DividerDefaults.Thickness,
                            color = DividerDefaults.color
                        )
                    }
                }
            }
        }
    }
}

// ----------------- Trip Plan (days list) -----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripPlanScreen(
    tripId: Long,
    tripVm: TripViewModel,
    onBack: () -> Unit
) {
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var newDayName by rememberSaveable { mutableStateOf("") }

    val trips by tripVm.trips.collectAsState()
    val trip = trips.firstOrNull { it.id == tripId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Text("Add day")
            }
        }
    ) { inner ->
        if (trip == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                contentAlignment = Alignment.Center
            ) {
                Text("Trip not found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                items(trip.days, key = { it.id }) { day ->
                    ListItem(
                        headlineContent = { Text(day.name) },
                        trailingContent = {
                            IconButton(onClick = { tripVm.deleteDay(trip.id, day.id) }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete day"
                                )
                            }
                        }
                    )
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }

                if (trip.days.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No days yet. Use “Add day” to create one.")
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add day") },
            text = {
                TextField(
                    value = newDayName,
                    onValueChange = { newDayName = it },
                    label = { Text("Day name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val trimmed = newDayName.trim()
                        if (trimmed.isNotEmpty() && trip != null) {
                            tripVm.addDay(trip.id, trimmed)
                        }
                        newDayName = ""
                        showAddDialog = false
                    }
                ) { Text("Add") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        newDayName = ""
                        showAddDialog = false
                    }
                ) { Text("Cancel") }
            }
        )
    }
}
