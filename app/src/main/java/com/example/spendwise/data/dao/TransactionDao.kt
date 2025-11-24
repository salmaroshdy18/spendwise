package com.example.spendwise.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.spendwise.data.entity.TransactionEntity


@Dao
interface TransactionDao {
    data class CategoryTotal(
        val category: String,
        val total: Double
    )

    data class MonthTotal(
        val month: String,
        val total: Double
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tx: TransactionEntity)

    @Delete
    suspend fun delete(tx: TransactionEntity)

    // Return LiveData for observing list in ViewModel
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): LiveData<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income'")
    fun getTotalIncome(): LiveData<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense'")
    fun getTotalExpense(): LiveData<Double?>

    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE type = 'expense' GROUP BY category")
    fun getExpenseByCategory(): LiveData<List<CategoryTotal>>

    @Query("SELECT strftime('%Y-%m', date/1000, 'unixepoch') as month, SUM(amount) as total " +
            "FROM transactions WHERE type = 'expense' GROUP BY month ORDER BY month")
    fun getMonthlyExpense(): LiveData<List<MonthTotal>>

    @Update
    suspend fun update(tx: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): TransactionEntity?

}
