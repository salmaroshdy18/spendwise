package com.example.spendwise.ui.goals

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendwise.R
import com.example.spendwise.viewmodel.GoalViewModel
import android.widget.Toast
import com.example.spendwise.data.entity.GoalEntity



class GoalsFragment : Fragment(R.layout.fragment_goals) {

    private val vm: GoalViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnAdd: Button = view.findViewById(R.id.btnAddGoal)
        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvGoals)

        val adapter = GoalAdapter(
            onGoalClick = { goal -> showUpdateDialog(goal) },
            onGoalLongClick = { goal -> showDeleteDialog(goal) }
        )
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        vm.allGoals.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        btnAdd.setOnClickListener { showAddDialog() }
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_goal, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etGoalTitle)
        val etTarget = dialogView.findViewById<EditText>(R.id.etGoalAmount)

        AlertDialog.Builder(requireContext())
            .setTitle("Add Goal")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = etTitle.text.toString().trim()
                val target = etTarget.text.toString().trim().toDoubleOrNull()

                if (title.isEmpty()) {
                    Toast.makeText(requireContext(), "Enter goal title", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (target == null || target <= 0.0) {
                    Toast.makeText(requireContext(), "Enter a valid positive target", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                vm.addGoal(title, target, null)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showUpdateDialog(goal: GoalEntity) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_goal, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etGoalTitle)
        val etTarget = dialogView.findViewById<EditText>(R.id.etGoalAmount)

        etTitle.setText(goal.title)
        etTarget.setText("") // we use this as "add more saved"

        AlertDialog.Builder(requireContext())
            .setTitle("Add to saved amount")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val extra = etTarget.text.toString().trim().toDoubleOrNull()
                if (extra == null || extra <= 0.0) {
                    Toast.makeText(requireContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val updated = goal.copy(currentSaved = goal.currentSaved + extra)
                vm.updateGoal(updated)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteDialog(goal: GoalEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete goal")
            .setMessage("Are you sure you want to delete \"${goal.title}\"?")
            .setPositiveButton("Delete") { _, _ ->
                vm.deleteGoal(goal)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}
