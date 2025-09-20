package com.example.flightplanner.data

import com.example.flightplanner.model.Trip
import com.example.flightplanner.model.TripChecklist
import kotlinx.coroutines.flow.StateFlow

interface TripRepository {
    val trips: StateFlow<List<Trip>>

    fun addTrip(name: String, checklists: List<TripChecklist> = emptyList()): Long
    fun deleteTrip(id: Long)
    fun toggleTripChecklistItem(tripId: Long, checklistId: Long, categoryId: Long, itemId: Long)
}
