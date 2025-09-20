package com.example.flightplanner.model

data class Trip(
    val id: Long,
    val name: String,
    val checklists: List<TripChecklist> = emptyList(),
    val days: List<TripDay> = emptyList()
)

data class TripDay(
    val id: Long,
    val name: String
)

data class TripChecklist(
    val id: Long,
    val name: String,
    val categories: List<TripChecklistCategory>
)

data class TripChecklistCategory(
    val id: Long,
    val name: String,
    val items: List<TripChecklistItem>
)

data class TripChecklistItem(
    val id: Long,
    val text: String,
    val checked: Boolean = false
)
