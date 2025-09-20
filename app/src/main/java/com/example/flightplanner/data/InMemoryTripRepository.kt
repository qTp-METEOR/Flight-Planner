package com.example.flightplanner.data

import com.example.flightplanner.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.atomic.AtomicLong

class InMemoryTripRepository : TripRepository {
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    override val trips: StateFlow<List<Trip>> = _trips

    private val idGen = AtomicLong(1)

    override fun addTrip(name: String): Long {
        val id = idGen.getAndIncrement()
        val trip = Trip(id, name)
        _trips.value = _trips.value + trip
        return id
    }

    override fun deleteTrip(id: Long) {
        _trips.value = _trips.value.filterNot { it.id == id }
    }
}
