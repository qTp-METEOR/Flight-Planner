package com.example.flightplanner.data

import com.example.flightplanner.model.PlannerDay
import com.example.flightplanner.model.PlannerEntry
import kotlinx.coroutines.flow.StateFlow

interface PlannerRepository {
    val days: StateFlow<List<PlannerDay>>
    fun addDay(): Long
    fun getDayById(id: Long): PlannerDay?
    fun replaceDay(updated: PlannerDay)
    fun addEntry(dayId: Long, entry: PlannerEntry)
}
