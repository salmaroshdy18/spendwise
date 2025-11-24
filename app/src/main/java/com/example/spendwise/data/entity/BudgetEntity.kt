package com.example.spendwise.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryName: String,     // link to CategoryEntity.name
    val month: String,            // e.g., "2025-11"
    val limitAmount: Double
)
