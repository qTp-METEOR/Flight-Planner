package com.example.flightplanner.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.flightplanner.model.PlannerEntry
import com.example.flightplanner.model.TripDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicLong

class InMemoryPlannerRepository : PlannerRepository {
    private val idGen = AtomicLong(1L)
    private val _days = MutableStateFlow<List<TripDay>>(emptyList())
    override val days: StateFlow<List<TripDay>> = _days

    @RequiresApi(Build.VERSION_CODES.O)
    override fun addDay(): Long {
        val id = idGen.getAndIncrement()
        val nextDate = (_days.value.maxByOrNull { it.date }?.date ?: LocalDate.now()).plusDays(1)
        val newDay = TripDay(id = id, date = nextDate, entries = emptyList())
        _days.value = _days.value + newDay
        return id
    }

    override fun getDayById(id: Long): TripDay? = _days.value.firstOrNull { it.id == id }

    override fun replaceDay(updated: TripDay) {
        _days.value = _days.value.map { if (it.id == updated.id) updated else it }
    }

    override fun addEntry(dayId: Long, entry: PlannerEntry) {
        val day = getDayById(dayId) ?: return
        replaceDay(day.copy(entries = day.entries + entry))
    }
}
