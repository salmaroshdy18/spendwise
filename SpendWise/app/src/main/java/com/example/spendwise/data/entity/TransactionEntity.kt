package com.example.spendwise.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val amount: Double,
    val type: String,       // "income" or "expense"
    val category: String,   // groceries, bills, salary, etc.
    val date: Long          // timestamp (System.currentTimeMillis())
)
