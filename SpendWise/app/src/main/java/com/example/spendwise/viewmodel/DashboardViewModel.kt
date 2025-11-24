package com.example.spendwise.viewmodel

import androidx.lifecycle.*
import com.example.spendwise.data.entity.TransactionEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DashboardViewModel : ViewModel() {

    // -----------------------------
    //  SYNTHETIC SAMPLE DASHBOARD DATA
    // -----------------------------
    private val sampleTransactions = listOf(
        TransactionEntity(
            title = "Grocery",
            amount = 45.0,
            category = "Food",
            type = "expense",
            date = System.currentTimeMillis(),
        ),
        TransactionEntity(
            title = "Salary",
            amount = 5000.0,
            category = "Income",
            type = "income",
            date = System.currentTimeMillis(),
        ),
        TransactionEntity(
            title = "Coffee",
            amount = 12.5,
            category = "Food",
            type = "expense",
            date = System.currentTimeMillis(),
        ),
        TransactionEntity(
            title = "Gym",
            amount = 120.0,
            category = "Health",
            type = "expense",
            date = System.currentTimeMillis(),
        ),
        TransactionEntity(
            title = "Internet Bill",
            amount = 250.0,
            category = "Utilities",
            type = "expense",
            date = System.currentTimeMillis(),
        )
    )

    // -----------------------------
    //  LIVE DATA FOR DASHBOARD
    // -----------------------------
    private val _totalIncome = MutableLiveData<Double>()
    val totalIncome: LiveData<Double> get() = _totalIncome

    private val _totalExpense = MutableLiveData<Double>()
    val totalExpense: LiveData<Double> get() = _totalExpense

    private val _latestTransactions = MutableLiveData<List<TransactionEntity>>()
    val latestTransactions: LiveData<List<TransactionEntity>> get() = _latestTransactions

    private val _dailyTrend = MutableLiveData<List<Pair<String, Float>>>()
    val dailyTrend: LiveData<List<Pair<String, Float>>> get() = _dailyTrend

    private val _categoryTotals = MutableLiveData<Map<String, Float>>()
    val categoryTotals: LiveData<Map<String, Float>> get() = _categoryTotals

    private val _monthlyTotals = MutableLiveData<Map<String, Float>>()
    val monthlyTotals: LiveData<Map<String, Float>> get() = _monthlyTotals

    // ----------------------------------------------------
    // LOAD SYNTHETIC DASHBOARD VALUES
    // ----------------------------------------------------
    fun loadDashboardData() = viewModelScope.launch {

        val income = sampleTransactions.filter { it.type == "income" }.sumOf { it.amount }
        val expense = sampleTransactions.filter { it.type == "expense" }.sumOf { it.amount }

        _totalIncome.value = income
        _totalExpense.value = expense

        // LATEST 5 ITEMS
        _latestTransactions.value = sampleTransactions.takeLast(5)

        // DAILY TREND
        val df = SimpleDateFormat("dd/MM", Locale.getDefault())
        val trendMap = mutableMapOf<String, Float>()

        sampleTransactions.filter { it.type == "expense" }.forEach {
            val day = df.format(Date(it.date))
            trendMap[day] = (trendMap[day] ?: 0f) + it.amount.toFloat()
        }
        _dailyTrend.value = trendMap.map { it.key to it.value }

        // CATEGORY TOTALS
        val catMap = mutableMapOf<String, Float>()
        sampleTransactions.filter { it.type == "expense" }.forEach {
            catMap[it.category] = (catMap[it.category] ?: 0f) + it.amount.toFloat()
        }
        _categoryTotals.value = catMap

        // MONTHLY TOTALS
        val mf = SimpleDateFormat("MMM", Locale.getDefault())
        val monthMap = mutableMapOf<String, Float>()

        sampleTransactions.filter { it.type == "expense" }.forEach {
            val month = mf.format(Date(it.date))
            monthMap[month] = (monthMap[month] ?: 0f) + it.amount.toFloat()
        }
        _monthlyTotals.value = monthMap
    }
}
