package com.example.spendwise.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.spendwise.data.database.AppDatabase
import com.example.spendwise.data.entity.BudgetEntity
import com.example.spendwise.data.repository.BudgetRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class BudgetViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: BudgetRepository
    lateinit var budgets: LiveData<List<BudgetEntity>>

    init {
        val dao = AppDatabase.getInstance(app).budgetDao()
        repository = BudgetRepository(dao)
        loadCurrentMonth()
    }

    fun loadCurrentMonth() {
        val month = LocalDate.now().toString().substring(0, 7) // "YYYY-MM"
        budgets = repository.getBudgetsForMonth(month)
    }

    fun addBudget(categoryName: String, limit: Double) = viewModelScope.launch {
        val month = LocalDate.now().toString().substring(0, 7)
        repository.insert(
            BudgetEntity(
                categoryName = categoryName,
                month = month,
                limitAmount = limit
            )
        )
    }

    fun deleteBudget(budget: BudgetEntity) = viewModelScope.launch {
        repository.delete(budget)
    }
}
