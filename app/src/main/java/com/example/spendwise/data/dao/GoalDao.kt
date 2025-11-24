package com.example.spendwise.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.spendwise.data.entity.GoalEntity

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals ORDER BY id DESC")
    fun getAllGoals(): LiveData<List<GoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: GoalEntity)

    @Update
    suspend fun update(goal: GoalEntity)

    @Delete
    suspend fun delete(goal: GoalEntity)
}
