package com.example.flightplanner.data

import com.example.flightplanner.model.ChecklistItem
import com.example.flightplanner.model.ChecklistTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.atomic.AtomicLong

class InMemoryChecklistRepository : ChecklistRepository {
    private val _templates = MutableStateFlow<List<ChecklistTemplate>>(emptyList())
    override val templates: StateFlow<List<ChecklistTemplate>> = _templates

    private val templateIdGen = AtomicLong(1)
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

    override fun addItem(templateId: Long, text: String) {
        val template = _templates.value.firstOrNull { it.id == templateId } ?: return
        val item = ChecklistItem(id = itemIdGen.getAndIncrement(), text = text)
        val updated = template.copy(items = template.items + item)
        updateTemplate(updated)
    }

    override fun removeItem(templateId: Long, itemId: Long) {
        val template = _templates.value.firstOrNull { it.id == templateId } ?: return
        val updated = template.copy(items = template.items.filterNot { it.id == itemId })
        updateTemplate(updated)
    }
}
