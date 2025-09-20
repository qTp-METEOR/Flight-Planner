package com.example.flightplanner.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.flightplanner.model.PlannerDay
import com.example.flightplanner.model.PlannerEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicLong

class InMemoryPlannerRepository : PlannerRepository {
    private val idGen = AtomicLong(1L)
    private val _days = MutableStateFlow<List<PlannerDay>>(emptyList())
    override val days: StateFlow<List<PlannerDay>> = _days

    @RequiresApi(Build.VERSION_CODES.O)
    override fun addDay(): Long {
        val id = idGen.getAndIncrement()
        val newDay = PlannerDay(id = id, date = LocalDate.now(), entries = emptyList())
        _days.value = _days.value + newDay
        return id
    }

    override fun getDayById(id: Long): PlannerDay? = _days.value.firstOrNull { it.id == id }

    override fun replaceDay(updated: PlannerDay) {
        _days.value = _days.value.map { if (it.id == updated.id) updated else it }
    }

    override fun addEntry(dayId: Long, entry: PlannerEntry) {
        val day = getDayById(dayId) ?: return
        replaceDay(day.copy(entries = day.entries + entry))
    }
}
