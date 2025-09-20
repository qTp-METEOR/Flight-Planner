package com.example.flightplanner.model

data class ChecklistTemplate(
    val id: Long,
    val name: String,
    val items: List<ChecklistItem>
)

data class ChecklistItem(
    val id: Long,
    val text: String
)
