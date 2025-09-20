package com.example.flightplanner.model

import java.time.LocalDate
import java.time.LocalTime

data class TripDay(
    val id: Long,
    val date: LocalDate,
    val entries: List<PlannerEntry>
)

data class PlannerEntry(
    val id: Long,
    val type: EntryType,
    val title: String,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val notes: String = "",
    val location: GeoPoint? = null
)

enum class EntryType { HOTEL, FLIGHT, ATTRACTION, OTHER }

data class GeoPoint(val lat: Double, val lng: Double)
