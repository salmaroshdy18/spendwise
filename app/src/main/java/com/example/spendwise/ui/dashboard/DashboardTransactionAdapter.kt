package com.example.spendwise.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spendwise.databinding.ItemTransactionBinding
import com.example.spendwise.data.entity.TransactionEntity

class DashboardTransactionAdapter(
    private val list: List<TransactionEntity>,
    private val onItemClick: (TransactionEntity) -> Unit = {},
    private val onItemLongClick: (TransactionEntity) -> Unit = {}
) : RecyclerView.Adapter<DashboardTransactionAdapter.TViewHolder>() {

    inner class TViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTransactionBinding.inflate(inflater, parent, false)
        return TViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvTitle.text = item.title
        holder.binding.tvCategory.text = item.category

        if (item.type == "income") {
            holder.binding.tvAmount.text = "+ AED ${item.amount}"
            holder.binding.tvAmount.setTextColor(0xFF22C55E.toInt()) // green
        } else {
            holder.binding.tvAmount.text = "- AED ${item.amount}"
            holder.binding.tvAmount.setTextColor(0xFFE44444.toInt()) // red
        }

        holder.binding.root.setOnClickListener {
            onItemClick(item)
        }

        holder.binding.root.setOnLongClickListener {
            onItemLongClick(item)
            true
        }
    }

    override fun getItemCount(): Int = list.size
}
