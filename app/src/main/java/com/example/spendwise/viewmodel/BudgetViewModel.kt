package com.example.spendwise.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.spendwise.data.database.AppDatabase
import com.example.spendwise.data.entity.BudgetEntity
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.data.repository.BudgetRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

class BudgetViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: BudgetRepository
    lateinit var budgets: LiveData<List<BudgetEntity>>

    // All transactions from DB (used to calculate spending)
    private val transactionDao = AppDatabase.getInstance(app).transactionDao()
    private val allTransactions: LiveData<List<TransactionEntity>> =
        transactionDao.getAllTransactions()

    // UI model: one row per budget with usage info
    data class BudgetWithUsage(
        val categoryName: String,
        val month: String,
        val limitAmount: Double,
        val spent: Double
    )

    private val _budgetItems = MediatorLiveData<List<BudgetWithUsage>>()
    val budgetItems: LiveData<List<BudgetWithUsage>> = _budgetItems

    init {
        val dao = AppDatabase.getInstance(app).budgetDao()
        repository = BudgetRepository(dao)
        loadCurrentMonth()

        // When transactions change → recalc
        _budgetItems.addSource(allTransactions) { txList ->
            recalcBudgetItems(txList, budgets.value)
        }

        // When budgets change → recalc
        _budgetItems.addSource(budgets) { budgetList ->
            recalcBudgetItems(allTransactions.value, budgetList)
        }
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

    // Combine budgets + transactions to produce UI rows
    private fun recalcBudgetItems(
        txList: List<TransactionEntity>?,
        budgetsList: List<BudgetEntity>?
    ) {
        val bList = budgetsList ?: return
        val transactions = txList ?: emptyList()

        val result = bList.map { budget ->
            val spent = transactions
                .filter { tx ->
                    tx.type == "expense" &&
                            tx.category == budget.categoryName &&
                            sameMonth(tx.date, budget.month)
                }
                .sumOf { it.amount }

            BudgetWithUsage(
                categoryName = budget.categoryName,
                month = budget.month,
                limitAmount = budget.limitAmount,
                spent = spent
            )
        }

        _budgetItems.value = result
    }

    private fun sameMonth(timestamp: Long, monthString: String): Boolean {
        // monthString is "YYYY-MM"
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1 // 1–12
        val formatted = String.format("%04d-%02d", year, month)
        return formatted == monthString
    }
}
