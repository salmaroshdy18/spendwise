package com.example.spendwise.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.spendwise.data.database.AppDatabase
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: TransactionRepository

    val transactions: LiveData<List<TransactionEntity>>
    val income: LiveData<Double?>
    val expense: LiveData<Double?>

    init {
        val dao = AppDatabase.getInstance(app).transactionDao()
        repository = TransactionRepository(dao)

        transactions = repository.allTransactions
        income = repository.totalIncome
        expense = repository.totalExpense
    }

    fun add(tx: TransactionEntity) = viewModelScope.launch {
        repository.insert(tx)
    }

    fun remove(tx: TransactionEntity) = viewModelScope.launch {
        repository.delete(tx)
    }

    fun update(tx: TransactionEntity) = viewModelScope.launch {
        repository.update(tx)
    }

    suspend fun getTransactionById(id: Int): TransactionEntity? {
        return repository.getById(id)
    }

}
