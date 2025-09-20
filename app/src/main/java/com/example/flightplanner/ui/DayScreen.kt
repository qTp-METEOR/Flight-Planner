package com.example.flightplanner.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightplanner.model.TripDay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaysScreen(
    state: kotlinx.coroutines.flow.StateFlow<List<TripDay>>,
    onAddDay: () -> Unit,
    onOpenDay: (Long) -> Unit
) {
    val days by state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip Days") },
                actions = {
                    IconButton(onClick = { /* settings or future menu */ }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDay) {
                Icon(Icons.Default.Add, contentDescription = "Add day")
            }
        }
    ) { inner ->
        if (days.isEmpty()) {
            EmptyDaysHint(Modifier.fillMaxSize())
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(top = inner.calculateTopPadding(), bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(days) { day ->
                    DayRow(day = day, onClick = { onOpenDay(day.id) })
                }
            }
        }
    }
}

@Composable
private fun DayRow(day: TripDay, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(text = day.date.toString(), style = MaterialTheme.typography.titleMedium)
        Text(text = "${day.entries.size} planned items", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun EmptyDaysHint(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "No days yet. Tap + to add the first day.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
