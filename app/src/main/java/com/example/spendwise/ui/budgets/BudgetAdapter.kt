package com.example.spendwise.ui.budgets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spendwise.R
import com.example.spendwise.viewmodel.BudgetViewModel

class BudgetAdapter : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    private var items: List<BudgetViewModel.BudgetWithUsage> = emptyList()

    fun submitList(newItems: List<BudgetViewModel.BudgetWithUsage>) {
        items = newItems
        notifyDataSetChanged()
    }

    class BudgetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvLimit: TextView = view.findViewById(R.id.tvLimit)
        val tvSpent: TextView = view.findViewById(R.id.tvSpent)
        val tvRemaining: TextView = view.findViewById(R.id.tvRemaining)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_budget, parent, false)
        return BudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val item = items[position]

        val limit = item.limitAmount
        val spent = item.spent
        val remaining = (limit - spent).coerceAtLeast(0.0)
        val percentUsed = if (limit > 0) ((spent / limit) * 100).toInt() else 0

        holder.tvCategory.text = item.categoryName
        holder.tvLimit.text = "Limit: %.2f AED".format(limit)
        holder.tvSpent.text = "Spent: %.2f AED".format(spent)
        holder.tvRemaining.text =
            "Remaining: %.2f AED (%d%% used)".format(remaining, percentUsed)
    }

    override fun getItemCount(): Int = items.size
}
