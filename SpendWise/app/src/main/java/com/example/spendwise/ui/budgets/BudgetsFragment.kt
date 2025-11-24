package com.example.spendwise.ui.budgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendwise.R
import com.example.spendwise.data.entity.BudgetEntity
import com.example.spendwise.viewmodel.BudgetViewModel
import com.example.spendwise.viewmodel.CategoryViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class BudgetsFragment : Fragment() {

    private val budgetVm: BudgetViewModel by viewModels()
    private val categoryVm: CategoryViewModel by viewModels()

    private lateinit var dropdownCategory: AutoCompleteTextView
    private lateinit var inputLimit: TextInputEditText
    private lateinit var btnSave: MaterialButton
    private lateinit var adapter: BudgetAdapter
    private lateinit var recycler: androidx.recyclerview.widget.RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_budgets, container, false)

        dropdownCategory = view.findViewById(R.id.dropdownCategory)
        inputLimit = view.findViewById(R.id.inputLimit)
        btnSave = view.findViewById(R.id.btnSaveBudget)
        recycler = view.findViewById(R.id.recyclerBudgets)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Setup RecyclerView
        adapter = BudgetAdapter()
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        // 2) Observe budgets from ViewModel
        budgetVm.budgets.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list ?: emptyList())
        }

        // 3) Load categories into dropdown
        categoryVm.allCategories.observe(viewLifecycleOwner) { list ->
            val names = list.map { it.name }

            val arrayAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                names
            )
            dropdownCategory.setAdapter(arrayAdapter)
        }

        // 4) Save button logic
        btnSave.setOnClickListener {
            val categoryName = dropdownCategory.text.toString().trim()
            val limitText = inputLimit.text?.toString()?.trim()

            if (categoryName.isEmpty() || limitText.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val limit = limitText.toDoubleOrNull()
            if (limit == null) {
                Toast.makeText(requireContext(), "Limit must be a number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ViewModel handles month itself (if you coded it that way)
            budgetVm.addBudget(categoryName, limit)

            Toast.makeText(requireContext(), "Budget saved", Toast.LENGTH_SHORT).show()

            // Clear inputs
            dropdownCategory.setText("")
            inputLimit.setText("")
        }
    }
}
