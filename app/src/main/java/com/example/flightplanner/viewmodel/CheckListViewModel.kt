package com.example.flightplanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightplanner.data.ChecklistRepository
import com.example.flightplanner.model.ChecklistTemplate
import kotlinx.coroutines.flow.StateFlow

class ChecklistViewModel(private val repo: ChecklistRepository) : ViewModel() {
    val templates: StateFlow<List<ChecklistTemplate>> = repo.templates

    fun addTemplate(name: String): Long = repo.addTemplate(name)
    fun updateTemplate(template: ChecklistTemplate) = repo.updateTemplate(template)
    fun deleteTemplate(id: Long) = repo.deleteTemplate(id)
    fun addItem(templateId: Long, text: String) = repo.addItem(templateId, text)
    fun removeItem(templateId: Long, itemId: Long) = repo.removeItem(templateId, itemId)
}

class ChecklistViewModelFactory(
    private val repo: ChecklistRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChecklistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChecklistViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
