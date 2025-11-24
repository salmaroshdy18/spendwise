package com.example.spendwise.data.repository

import com.example.spendwise.data.dao.TransactionDao
import com.example.spendwise.data.entity.TransactionEntity

class TransactionRepository(private val dao: TransactionDao) {

    // Existing
    val allTransactions = dao.getAllTransactions()
    val totalIncome = dao.getTotalIncome()
    val totalExpense = dao.getTotalExpense()

    // NEW: extra data for dashboard
    val expenseByCategory = dao.getExpenseByCategory()
    val monthlyExpense = dao.getMonthlyExpense()

    suspend fun insert(tx: TransactionEntity) = dao.insert(tx)
    suspend fun delete(tx: TransactionEntity) = dao.delete(tx)
    suspend fun update(tx: TransactionEntity) = dao.update(tx)
    suspend fun getById(id: Int): TransactionEntity? = dao.getById(id)

}
