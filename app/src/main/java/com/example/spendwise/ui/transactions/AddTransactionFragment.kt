package com.example.spendwise.ui.transactions

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spendwise.R
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.viewmodel.TransactionViewModel
import com.example.spendwise.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionFragment : Fragment() {

    private val vm: TransactionViewModel by viewModels()
    private val categoryVm: CategoryViewModel by viewModels()

    private lateinit var etTitle: EditText
    private lateinit var etAmount: EditText
    private lateinit var dropdownCategory: AutoCompleteTextView
    private lateinit var rbIncome: RadioButton
    private lateinit var rbExpense: RadioButton
    private lateinit var btnSave: Button
    private lateinit var etDate: EditText

    private var selectedDateMillis: Long = System.currentTimeMillis()
    private var editingId: Int? = null

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)

        etTitle = view.findViewById(R.id.etTitle)
        etAmount = view.findViewById(R.id.etAmount)
        dropdownCategory = view.findViewById(R.id.dropdownCategory)
        rbIncome = view.findViewById(R.id.rbIncome)
        rbExpense = view.findViewById(R.id.rbExpense)
        btnSave = view.findViewById(R.id.btnSave)
        etDate = view.findViewById(R.id.etDate)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if we are editing
        val txIdArg = arguments?.getInt("transactionId", -1) ?: -1
        editingId = if (txIdArg > 0) txIdArg else null

        if (editingId != null) {
            btnSave.text = "Update Transaction"
            loadExistingTransaction(editingId!!)
        } else {
            btnSave.text = "Save Transaction"
            etDate.setText(dateFormatter.format(Date(selectedDateMillis)))
        }

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

        // Date picker
        etDate.setOnClickListener {
            showDatePicker()
        }

        // Save / Update button
        btnSave.setOnClickListener {
            saveOrUpdate()
        }
    }

    private fun loadExistingTransaction(id: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            val existing = vm.getTransactionById(id)
            if (existing != null) {
                etTitle.setText(existing.title)
                etAmount.setText(existing.amount.toString())
                dropdownCategory.setText(existing.category, false)

                if (existing.type == "income") {
                    rbIncome.isChecked = true
                } else {
                    rbExpense.isChecked = true
                }

                selectedDateMillis = existing.date
                etDate.setText(dateFormatter.format(Date(existing.date)))
            }
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = selectedDateMillis

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val newCal = Calendar.getInstance()
                newCal.set(year, month, dayOfMonth, 0, 0, 0)
                selectedDateMillis = newCal.timeInMillis
                etDate.setText(dateFormatter.format(Date(selectedDateMillis)))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun saveOrUpdate() {
        val title = etTitle.text.toString().trim()
        val amountText = etAmount.text.toString().trim()
        val category = dropdownCategory.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            Toast.makeText(requireContext(), "Enter a valid positive amount", Toast.LENGTH_SHORT).show()
            return
        }

        if (category.isEmpty()) {
            Toast.makeText(requireContext(), "Please choose a category", Toast.LENGTH_SHORT).show()
            return
        }

        val type = if (rbIncome.isChecked) "income" else "expense"

        val tx = TransactionEntity(
            id = editingId ?: 0,
            title = title,
            amount = amount,
            type = type,
            category = category,
            date = selectedDateMillis
        )

        if (editingId == null) {
            vm.add(tx)
            Toast.makeText(requireContext(), "Transaction saved", Toast.LENGTH_SHORT).show()
        } else {
            vm.update(tx)
            Toast.makeText(requireContext(), "Transaction updated", Toast.LENGTH_SHORT).show()
        }

        findNavController().navigateUp()
    }
}
