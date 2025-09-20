package com.example.flightplanner.model

data class ChecklistTemplate(
    val id: Long,
    val name: String,
    val categories: List<ChecklistCategory>
)

data class ChecklistCategory(
    val id: Long,
    val name: String,
    val items: List<ChecklistItem>
)

data class ChecklistItem(
    val id: Long,
    val text: String
)
