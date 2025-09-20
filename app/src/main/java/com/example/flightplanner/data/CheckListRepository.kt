package com.example.flightplanner.data

import com.example.flightplanner.model.ChecklistTemplate
import kotlinx.coroutines.flow.StateFlow

interface ChecklistRepository {
    val templates: StateFlow<List<ChecklistTemplate>>

    fun addTemplate(name: String): Long
    fun updateTemplate(template: ChecklistTemplate)
    fun deleteTemplate(id: Long)

    fun addItem(templateId: Long, text: String)
    fun removeItem(templateId: Long, itemId: Long)
}
