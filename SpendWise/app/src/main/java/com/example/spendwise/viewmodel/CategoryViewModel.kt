package com.example.spendwise.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.spendwise.data.database.AppDatabase
import com.example.spendwise.data.entity.CategoryEntity
import com.example.spendwise.data.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: CategoryRepository
    val allCategories: LiveData<List<CategoryEntity>>

    init {
        val dao = AppDatabase.getInstance(app).categoryDao()
        repository = CategoryRepository(dao)
        allCategories = repository.getAll()
    }

    fun addCategory(name: String, type: String) = viewModelScope.launch {
        repository.insert(CategoryEntity(name = name, type = type))
    }

    fun deleteCategory(category: CategoryEntity) = viewModelScope.launch {
        repository.delete(category)
    }
}
