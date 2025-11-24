package com.example.spendwise.data.repository

import com.example.spendwise.data.dao.BudgetDao
import com.example.spendwise.data.entity.BudgetEntity

class BudgetRepository(private val dao: BudgetDao) {

    fun getBudgetsForMonth(month: String) = dao.getBudgetsForMonth(month)

    suspend fun insert(budget: BudgetEntity) = dao.insert(budget)

    suspend fun delete(budget: BudgetEntity) = dao.delete(budget)
}
