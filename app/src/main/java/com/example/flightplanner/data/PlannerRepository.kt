package com.example.flightplanner.data

import com.example.flightplanner.model.PlannerEntry
import com.example.flightplanner.model.TripDay
import kotlinx.coroutines.flow.StateFlow

interface PlannerRepository {
    val days: StateFlow<List<TripDay>>
    fun addDay(): Long
    fun getDayById(id: Long): TripDay?
    fun replaceDay(updated: TripDay)
    fun addEntry(dayId: Long, entry: PlannerEntry)
}
