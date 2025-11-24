package com.example.spendwise.data.repository

import com.example.spendwise.data.dao.CategoryDao
import com.example.spendwise.data.entity.CategoryEntity

class CategoryRepository(private val dao: CategoryDao) {

    fun getAll() = dao.getAllCategories()

    fun getByType(type: String) = dao.getCategoriesByType(type)

    suspend fun insert(category: CategoryEntity) = dao.insert(category)

    suspend fun delete(category: CategoryEntity) = dao.delete(category)
}
