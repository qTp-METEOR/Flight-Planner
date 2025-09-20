package com.example.flightplanner.data

import com.example.flightplanner.model.ChecklistCategory
import com.example.flightplanner.model.ChecklistItem
import com.example.flightplanner.model.ChecklistTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.atomic.AtomicLong

class InMemoryChecklistRepository : ChecklistRepository {
    private val _templates = MutableStateFlow<List<ChecklistTemplate>>(emptyList())
    override val templates: StateFlow<List<ChecklistTemplate>> = _templates

    private val templateIdGen = AtomicLong(1)
    private val categoryIdGen = AtomicLong(1)
    private val itemIdGen = AtomicLong(1)

    override fun addTemplate(name: String): Long {
        val id = templateIdGen.getAndIncrement()
        val newTemplate = ChecklistTemplate(id, name, emptyList())
        _templates.value = _templates.value + newTemplate
        return id
    }

    override fun updateTemplate(template: ChecklistTemplate) {
        _templates.value = _templates.value.map {
            if (it.id == template.id) template else it
        }
    }

    override fun deleteTemplate(id: Long) {
        _templates.value = _templates.value.filterNot { it.id == id }
    }

    override fun addCategory(templateId: Long, name: String): Long {
        val template = _templates.value.firstOrNull { it.id == templateId } ?: return -1
        val id = categoryIdGen.getAndIncrement()
        val newCategory = ChecklistCategory(id, name, emptyList())
        val updated = template.copy(categories = template.categories + newCategory)
        updateTemplate(updated)
        return id
    }

    override fun removeCategory(templateId: Long, categoryId: Long) {
        val template = _templates.value.firstOrNull { it.id == templateId } ?: return
        val updated = template.copy(categories = template.categories.filterNot { it.id == categoryId })
        updateTemplate(updated)
    }

    override fun addItem(templateId: Long, categoryId: Long, text: String): Long {
        val template = _templates.value.firstOrNull { it.id == templateId } ?: return -1
        val category = template.categories.firstOrNull { it.id == categoryId } ?: return -1
        val id = itemIdGen.getAndIncrement()
        val newItem = ChecklistItem(id, text)
        val updatedCategory = category.copy(items = category.items + newItem)
        val updated = template.copy(categories = template.categories.map {
            if (it.id == categoryId) updatedCategory else it
        })
        updateTemplate(updated)
        return id
    }

    override fun removeItem(templateId: Long, categoryId: Long, itemId: Long) {
        val template = _templates.value.firstOrNull { it.id == templateId } ?: return
        val category = template.categories.firstOrNull { it.id == categoryId } ?: return
        val updatedCategory = category.copy(items = category.items.filterNot { it.id == itemId })
        val updated = template.copy(categories = template.categories.map {
            if (it.id == categoryId) updatedCategory else it
        })
        updateTemplate(updated)
    }
}
