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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendwise.ui.dashboard.DashboardTransactionAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

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
        binding.rvLatestTransactions.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        setupCharts()
        observeViewModel()
        viewModel.loadDashboardData()
        setupCharts()
        observeViewModel()
        viewModel.loadDashboardData()
    }

    // Chart base styling
    private fun setupCharts() {
        binding.lineChartTrend.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            setDrawGridBackground(false)
        }

        binding.pieChartCategories.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            holeRadius = 55f
            transparentCircleRadius = 60f
            setUsePercentValues(true)
            setEntryLabelColor(Color.BLACK)
        }

        binding.barChartMonthly.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
        }
    }

    private fun observeViewModel() {

        viewModel.totalIncome.observe(viewLifecycleOwner) { value ->
            val safeValue = value ?: 0.0
            binding.tvIncome.text = getString(R.string.aed_amount, safeValue)
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) { value ->
            val safeValue = value ?: 0.0
            binding.tvExpense.text = getString(R.string.aed_amount, safeValue)
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

        viewModel.latestTransactions.observe(viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty()) {
                binding.rvLatestTransactions.adapter =
                    DashboardTransactionAdapter(list)
            }
        }

    }

    // ---------- Line Chart ----------
    private fun updateLineChart(list: List<Pair<String, Float>>) {
        val entries = list.mapIndexed { index, item ->
            Entry(index.toFloat(), item.second)
        }

        val dataSet = LineDataSet(entries, "Daily Spending").apply {
            color = ColorTemplate.COLORFUL_COLORS[0]
            valueTextColor = Color.BLACK
            lineWidth = 2f
            circleRadius = 3.5f
            setDrawFilled(true)
            fillColor = ColorTemplate.COLORFUL_COLORS[0]
        }

        binding.lineChartTrend.data = LineData(dataSet)
        binding.lineChartTrend.invalidate()
    }

    // ---------- Pie Chart ----------
    private fun updatePieChart(map: Map<String, Float>) {
        val entries = map.map { PieEntry(it.value, it.key) }

        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(
                ColorTemplate.MATERIAL_COLORS[0],
                ColorTemplate.MATERIAL_COLORS[1],
                ColorTemplate.MATERIAL_COLORS[2],
                ColorTemplate.MATERIAL_COLORS[3]
            )
            valueTextColor = Color.WHITE
            valueTextSize = 12f
        }

        binding.pieChartCategories.data = PieData(dataSet)
        binding.pieChartCategories.invalidate()
    }

    // ---------- Bar Chart ----------
    private fun updateBarChart(map: Map<String, Float>) {
        val entries = map.entries.mapIndexed { index, item ->
            BarEntry(index.toFloat(), item.value)
        }

        val dataSet = BarDataSet(entries, "Monthly Expense").apply {
            colors = ColorTemplate.COLORFUL_COLORS.toList()
            valueTextColor = Color.BLACK
            valueTextSize = 12f
        }

        binding.barChartMonthly.data = BarData(dataSet).apply {
            barWidth = 0.5f
        }
        binding.barChartMonthly.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
