package com.example.spendwise.ui.transactions

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.spendwise.R
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.viewmodel.TransactionViewModel
import com.example.spendwise.viewmodel.CategoryViewModel

class AddTransactionFragment : Fragment() {

    private val vm: TransactionViewModel by viewModels()
    private val categoryVm: CategoryViewModel by viewModels()

    private lateinit var etTitle: EditText
    private lateinit var etAmount: EditText
    private lateinit var dropdownCategory: AutoCompleteTextView
    private lateinit var rbIncome: RadioButton
    private lateinit var rbExpense: RadioButton
    private lateinit var btnSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)

        etTitle = view.findViewById(R.id.etTitle)
        etAmount = view.findViewById(R.id.etAmount)
        dropdownCategory = view.findViewById(R.id.dropdownCategory)
        rbIncome = view.findViewById(R.id.rbIncome)
        rbExpense = view.findViewById(R.id.rbExpense)
        btnSave = view.findViewById(R.id.btnSave)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Load categories into dropdown
        categoryVm.allCategories.observe(viewLifecycleOwner) { list ->
            val names = list.map { it.name }

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                names
            )

            dropdownCategory.setAdapter(adapter)
        }

        btnSave.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val amountText = etAmount.text.toString().trim()
            val category = dropdownCategory.text.toString().trim()

            if (title.isEmpty() || amountText.isEmpty() || category.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val type = if (rbIncome.isChecked) "income" else "expense"

            val tx = TransactionEntity(
                title = title,
                amount = amountText.toDouble(),
                type = type,
                category = category,
                date = System.currentTimeMillis()
            )

            vm.add(tx)

            Toast.makeText(requireContext(), "Transaction saved", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
}
