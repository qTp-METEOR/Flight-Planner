package com.example.flightplanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightplanner.data.TripRepository
import com.example.flightplanner.model.Trip
import com.example.flightplanner.model.TripChecklist
import kotlinx.coroutines.flow.StateFlow

class TripViewModel(private val repo: TripRepository) : ViewModel() {
    val trips: StateFlow<List<Trip>> = repo.trips

    fun addTrip(name: String, checklists: List<TripChecklist> = emptyList()): Long =
        repo.addTrip(name, checklists)
    fun deleteTrip(id: Long) = repo.deleteTrip(id)
    fun toggleTripChecklistItem(tripId: Long, checklistId: Long, categoryId: Long, itemId: Long) =
        repo.toggleTripChecklistItem(tripId, checklistId, categoryId, itemId)

    fun addDay(tripId: Long, name: String) = repo.addDay(tripId, name)
    fun deleteDay(tripId: Long, dayId: Long) = repo.deleteDay(tripId, dayId)
}

class TripViewModelFactory(
    private val repo: TripRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
