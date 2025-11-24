package com.example.spendwise.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.spendwise.R
import com.example.spendwise.databinding.FragmentDashboardBinding
import com.example.spendwise.viewmodel.DashboardViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // ViewModel with NO parameters now
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCharts()
        observeViewModel()
        viewModel.loadDashboardData()
    }

    private fun setupCharts() {
        binding.lineChartTrend.description.isEnabled = false
        binding.pieChartCategories.description.isEnabled = false
        binding.barChartMonthly.description.isEnabled = false
    }

    private fun observeViewModel() {

        viewModel.totalIncome.observe(viewLifecycleOwner) { value ->
            binding.tvIncome.text = getString(R.string.aed_amount, value)
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) { value ->
            binding.tvExpense.text = getString(R.string.aed_amount, value)
        }

        viewModel.dailyTrend.observe(viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty()) updateLineChart(list)
        }

        viewModel.categoryTotals.observe(viewLifecycleOwner) { map ->
            if (!map.isNullOrEmpty()) updatePieChart(map)
        }

        viewModel.monthlyTotals.observe(viewLifecycleOwner) { map ->
            if (!map.isNullOrEmpty()) updateBarChart(map)
        }
    }

    // ---------------- CHART UPDATE FUNCTIONS ----------------

    private fun updateLineChart(list: List<Pair<String, Float>>) {
        val entries = list.mapIndexed { index, item ->
            Entry(index.toFloat(), item.second)
        }

        val dataSet = LineDataSet(entries, "Daily Spending").apply {
            color = ColorTemplate.COLORFUL_COLORS[0]
            valueTextColor = Color.BLACK
            lineWidth = 2f
            circleRadius = 3f
        }

        binding.lineChartTrend.data = LineData(dataSet)
        binding.lineChartTrend.invalidate()
    }

    private fun updatePieChart(map: Map<String, Float>) {
        val entries = map.map { PieEntry(it.value, it.key) }

        val dataSet = PieDataSet(entries, "").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
        }

        binding.pieChartCategories.data = PieData(dataSet)
        binding.pieChartCategories.invalidate()
    }

    private fun updateBarChart(map: Map<String, Float>) {
        val entries = map.entries.mapIndexed { index, item ->
            BarEntry(index.toFloat(), item.value)
        }

        val dataSet = BarDataSet(entries, "Monthly Expense").apply {
            colors = ColorTemplate.COLORFUL_COLORS.toList()
        }

        binding.barChartMonthly.data = BarData(dataSet)
        binding.barChartMonthly.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
