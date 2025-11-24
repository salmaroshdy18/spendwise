package com.example.spendwise.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.spendwise.data.entity.BudgetEntity

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity)

    @Delete
    suspend fun delete(budget: BudgetEntity)

    @Query("SELECT * FROM budgets WHERE month = :month")
    fun getBudgetsForMonth(month: String): LiveData<List<BudgetEntity>>
}
