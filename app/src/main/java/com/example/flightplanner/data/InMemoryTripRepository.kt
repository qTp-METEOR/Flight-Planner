package com.example.flightplanner.data

import com.example.flightplanner.model.Trip
import com.example.flightplanner.model.TripChecklist
import com.example.flightplanner.model.TripDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicLong

class InMemoryTripRepository : TripRepository {
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    override val trips: StateFlow<List<Trip>> = _trips

    private val tripIdGen = AtomicLong(1)
    private val dayIdGen = AtomicLong(1) // counter for TripDay IDs

    override fun addTrip(name: String, checklists: List<TripChecklist>): Long {
        val id = tripIdGen.getAndIncrement()
        val trip = Trip(id, name, checklists)
        _trips.value = _trips.value + trip
        return id
    }

    override fun deleteTrip(id: Long) {
        _trips.value = _trips.value.filterNot { it.id == id }
    }

    override fun toggleTripChecklistItem(tripId: Long, checklistId: Long, categoryId: Long, itemId: Long) {
        val trip = _trips.value.firstOrNull { it.id == tripId } ?: return
        val updatedChecklists = trip.checklists.map { checklist ->
            if (checklist.id == checklistId) {
                val updatedCategories = checklist.categories.map { cat ->
                    if (cat.id == categoryId) {
                        val updatedItems = cat.items.map { item ->
                            if (item.id == itemId) item.copy(checked = !item.checked) else item
                        }
                        cat.copy(items = updatedItems)
                    } else cat
                }
                checklist.copy(categories = updatedCategories)
            } else checklist
        }
        val updatedTrip = trip.copy(checklists = updatedChecklists)
        _trips.value = _trips.value.map { if (it.id == tripId) updatedTrip else it }
    }

    override fun addDay(tripId: Long, name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return

        _trips.update { trips ->
            trips.map { trip ->
                if (trip.id == tripId) {
                    val newDay = TripDay(
                        id = dayIdGen.getAndIncrement(),
                        name = trimmed
                    )
                    trip.copy(days = trip.days + newDay)
                } else trip
            }
        }
    }

    override fun deleteDay(tripId: Long, dayId: Long) {
        _trips.update { trips ->
            trips.map { trip ->
                if (trip.id == tripId) {
                    trip.copy(days = trip.days.filterNot { it.id == dayId })
                } else trip
            }
        }
    }
}
