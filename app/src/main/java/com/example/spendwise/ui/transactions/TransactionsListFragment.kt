package com.example.spendwise.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendwise.R
import com.example.spendwise.databinding.FragmentTransactionsListBinding
import com.example.spendwise.ui.dashboard.DashboardTransactionAdapter
import com.example.spendwise.viewmodel.TransactionViewModel
import androidx.appcompat.app.AlertDialog
import android.os.Parcelable
import android.os.PersistableBundle


class TransactionsListFragment : Fragment() {

    private var _binding: FragmentTransactionsListBinding? = null
    private val binding get() = _binding!!

    private val vm: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())

        vm.transactions.observe(viewLifecycleOwner) { list ->
            binding.rvTransactions.adapter = DashboardTransactionAdapter(
                list,
                onItemClick = { tx ->
                    // Tap → edit
                    val bundle = Bundle().apply {
                        putInt("transactionId", tx.id)
                    }
                    findNavController().navigate(R.id.addTransactionFragment, bundle)
                },
                onItemLongClick = { tx ->
                    // Long press → delete confirm
                    AlertDialog.Builder(requireContext())
                        .setTitle("Delete transaction")
                        .setMessage("Are you sure you want to delete \"${tx.title}\"?")
                        .setPositiveButton("Delete") { _, _ ->
                            vm.remove(tx)
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            )
        }


        // ➜ Navigate to AddTransactionFragment
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.addTransactionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
