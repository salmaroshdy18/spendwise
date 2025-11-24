package com.example.spendwise.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spendwise.databinding.ItemTransactionBinding
import com.example.spendwise.data.entity.TransactionEntity

class DashboardTransactionAdapter(
    private val list: List<TransactionEntity>
) : RecyclerView.Adapter<DashboardTransactionAdapter.TViewHolder>() {

    inner class TViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TViewHolder, position: Int) {
        val item = list[position]

        // Title + Category
        holder.binding.tvTitle.text = item.title

        holder.binding.tvCategory.text = item.category   // or item.categoryName

        // Amount formatting
        if (item.type == "income") {
            holder.binding.tvAmount.text = "+ AED ${item.amount}"
            holder.binding.tvAmount.setTextColor(0xFF22C55E.toInt()) // green
        } else {
            holder.binding.tvAmount.text = "- AED ${item.amount}"
            holder.binding.tvAmount.setTextColor(0xFFE44444.toInt()) // red
        }
    }

    override fun getItemCount(): Int = list.size
}
