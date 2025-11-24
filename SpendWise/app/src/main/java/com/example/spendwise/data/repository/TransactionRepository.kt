package com.example.spendwise.data.repository

import com.example.spendwise.data.dao.TransactionDao
import com.example.spendwise.data.entity.TransactionEntity

class TransactionRepository(private val dao: TransactionDao) {

    val allTransactions = dao.getAllTransactions()
    val totalIncome = dao.getTotalIncome()
    val totalExpense = dao.getTotalExpense()

    suspend fun insert(tx: TransactionEntity) = dao.insert(tx)
    suspend fun delete(tx: TransactionEntity) = dao.delete(tx)
}
