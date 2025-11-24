package com.example.spendwise.data.repository

import com.example.spendwise.data.dao.GoalDao
import com.example.spendwise.data.entity.GoalEntity

class GoalRepository(private val dao: GoalDao) {

    val allGoals = dao.getAllGoals()

    suspend fun insert(goal: GoalEntity) = dao.insert(goal)
    suspend fun update(goal: GoalEntity) = dao.update(goal)
    suspend fun delete(goal: GoalEntity) = dao.delete(goal)
}
