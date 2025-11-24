package com.example.spendwise.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendwise.R
import com.example.spendwise.viewmodel.CategoryViewModel
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.RecyclerView

class ManageCategoriesFragment : Fragment() {

    private val categoryVm: CategoryViewModel by viewModels()

    private lateinit var etCategoryName: EditText
    private lateinit var rgType: RadioGroup
    private lateinit var rbIncome: RadioButton
    private lateinit var rbExpense: RadioButton
    private lateinit var btnSave: MaterialButton
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_manage_categories, container, false)

        etCategoryName = view.findViewById(R.id.etCategoryName)
        rgType = view.findViewById(R.id.rgType)
        rbIncome = view.findViewById(R.id.rbIncome)
        rbExpense = view.findViewById(R.id.rbExpense)
        btnSave = view.findViewById(R.id.btnSaveCategory)
        recycler = view.findViewById(R.id.recyclerCategories)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView setup
        adapter = CategoryAdapter { category ->
            categoryVm.deleteCategory(category)
            Toast.makeText(requireContext(), "Category deleted", Toast.LENGTH_SHORT).show()
        }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        // Observe categories
        categoryVm.allCategories.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list ?: emptyList())
        }

        // Save button
        btnSave.setOnClickListener {
            val name = etCategoryName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Enter category name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val type = if (rbIncome.isChecked) "income" else "expense"

            categoryVm.addCategory(name, type)

            Toast.makeText(requireContext(), "Category saved", Toast.LENGTH_SHORT).show()

            etCategoryName.setText("")
            rgType.clearCheck()
        }
    }
}
