package com.example.flightplanner.data

import com.example.flightplanner.model.ChecklistTemplate
import kotlinx.coroutines.flow.StateFlow

interface ChecklistRepository {
    val templates: StateFlow<List<ChecklistTemplate>>

    fun addTemplate(name: String): Long
    fun updateTemplate(template: ChecklistTemplate)
    fun deleteTemplate(id: Long)

    fun addCategory(templateId: Long, name: String): Long
    fun removeCategory(templateId: Long, categoryId: Long)

    fun addItem(templateId: Long, categoryId: Long, text: String): Long
    fun removeItem(templateId: Long, categoryId: Long, itemId: Long)
}
