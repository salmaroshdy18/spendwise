package com.example.spendwise.ui.goals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spendwise.R
import com.example.spendwise.data.entity.GoalEntity
import android.widget.ProgressBar


class GoalAdapter(
    private val onGoalClick: (GoalEntity) -> Unit,
    private val onGoalLongClick: (GoalEntity) -> Unit
) : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    private var items: List<GoalEntity> = emptyList()

    fun submitList(list: List<GoalEntity>) {
        items = list
        notifyDataSetChanged()
    }

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGoalTitle: TextView = itemView.findViewById(R.id.tvGoalTitle)
        val tvGoalProgress: TextView = itemView.findViewById(R.id.tvGoalProgress)
        val progressGoal: ProgressBar = itemView.findViewById(R.id.progressGoal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val item = items[position]

        val percent =
            if (item.targetAmount > 0) ((item.currentSaved / item.targetAmount) * 100).toInt()
            else 0

        holder.tvGoalTitle.text = item.title
        holder.tvGoalProgress.text =
            "Saved %.2f / %.2f AED".format(item.currentSaved, item.targetAmount)
        holder.progressGoal.progress = percent.coerceIn(0, 100)

        holder.itemView.setOnClickListener { onGoalClick(item) }
        holder.itemView.setOnLongClickListener {
            onGoalLongClick(item)
            true
        }
    }

    override fun getItemCount() = items.size
}
