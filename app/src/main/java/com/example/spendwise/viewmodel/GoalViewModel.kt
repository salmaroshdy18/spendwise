package com.example.spendwise.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.spendwise.data.database.AppDatabase
import com.example.spendwise.data.entity.GoalEntity
import com.example.spendwise.data.repository.GoalRepository
import kotlinx.coroutines.launch

class GoalViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: GoalRepository
    val allGoals: LiveData<List<GoalEntity>>

    init {
        val dao = AppDatabase.getInstance(app).goalDao()
        repository = GoalRepository(dao)
        allGoals = repository.allGoals
    }

    fun addGoal(title: String, target: Double, deadline: Long?) =
        viewModelScope.launch {
            repository.insert(
                GoalEntity(
                    title = title,
                    targetAmount = target,
                    deadline = deadline
                )
            )
        }

    fun updateGoal(goal: GoalEntity) =
        viewModelScope.launch { repository.update(goal) }

    fun deleteGoal(goal: GoalEntity) =
        viewModelScope.launch { repository.delete(goal) }
}
