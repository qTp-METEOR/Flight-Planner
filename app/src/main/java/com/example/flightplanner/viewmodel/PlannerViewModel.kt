package com.example.flightplanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightplanner.data.PlannerRepository
import com.example.flightplanner.model.PlannerDay
import kotlinx.coroutines.flow.StateFlow

class PlannerViewModel(private val repo: PlannerRepository) : ViewModel() {
    val days: StateFlow<List<PlannerDay>> = repo.days

    fun addDay(): Long = repo.addDay()

    fun dayById(id: Long): PlannerDay? = repo.getDayById(id)
}

class PlannerViewModelFactory(
    private val repo: PlannerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlannerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlannerViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
