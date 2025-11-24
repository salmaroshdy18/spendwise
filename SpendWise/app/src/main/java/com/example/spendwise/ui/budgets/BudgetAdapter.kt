package com.example.spendwise.ui.budgets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spendwise.R
import com.example.spendwise.data.entity.BudgetEntity

class BudgetAdapter : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    private var items: List<BudgetEntity> = emptyList()

    fun submitList(newItems: List<BudgetEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    class BudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvLimit: TextView = itemView.findViewById(R.id.tvLimit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_budget, parent, false)
        return BudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val item = items[position]
        holder.tvCategory.text = item.categoryName
        holder.tvLimit.text = "Limit: ${item.limitAmount}"
    }

    override fun getItemCount(): Int = items.size
}
