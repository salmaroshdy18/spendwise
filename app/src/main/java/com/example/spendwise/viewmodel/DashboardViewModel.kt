package com.example.spendwise.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.spendwise.data.database.AppDatabase
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.data.repository.TransactionRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getInstance(app).transactionDao()
    private val repository = TransactionRepository(dao)

    // ---- Public LiveData exposed to the fragment ----

    val totalIncome: LiveData<Double?> = repository.totalIncome
    val totalExpense: LiveData<Double?> = repository.totalExpense

    // Last 5 transactions
    val latestTransactions: LiveData<List<TransactionEntity>> =
        repository.allTransactions.map { list ->
            list.sortedByDescending { it.date }.take(5)
        }

    // Daily expense trend (label, amount)
    val dailyTrend: LiveData<List<Pair<String, Float>>> =
        repository.allTransactions.map { list ->
            val df = SimpleDateFormat("dd/MM", Locale.getDefault())
            val trendMap = linkedMapOf<String, Float>()

            list.filter { it.type == "expense" }.forEach { tx ->
                val day = df.format(Date(tx.date))
                trendMap[day] = (trendMap[day] ?: 0f) + tx.amount.toFloat()
            }

            trendMap.toList()  // List<Pair<String, Float>>
        }

    // Category totals from DAO (already aggregated)
    val categoryTotals: LiveData<Map<String, Float>> =
        repository.expenseByCategory.map { items ->
            items.associate { it.category to it.total.toFloat() }
        }

    // Monthly totals from DAO (yyyy-MM -> MMM)
    val monthlyTotals: LiveData<Map<String, Float>> =
        repository.monthlyExpense.map { items ->
            val inputFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM", Locale.getDefault())

            items.associate { mt ->
                val label = try {
                    val date = inputFormat.parse(mt.month)
                    if (date != null) outputFormat.format(date) else mt.month
                } catch (e: Exception) {
                    mt.month
                }
                label to mt.total.toFloat()
            }
        }

    // Kept so fragment code still compiles
    fun loadDashboardData() {
        // All data is reactive via LiveData from Room
    }
}
