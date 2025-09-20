package com.example.flightplanner.data

import com.example.flightplanner.model.Trip
import kotlinx.coroutines.flow.StateFlow

interface TripRepository {
    val trips: StateFlow<List<Trip>>

    fun addTrip(name: String): Long
    fun deleteTrip(id: Long)
}
