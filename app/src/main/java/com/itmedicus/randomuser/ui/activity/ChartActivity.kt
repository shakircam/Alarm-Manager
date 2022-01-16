package com.itmedicus.randomuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.itmedicus.randomuser.databinding.ActivityBmiBinding
import com.itmedicus.randomuser.databinding.ActivityChartBinding

class ChartActivity : AppCompatActivity() {


    private val bmiList = listOf(200,300,400,600)
    private val month = listOf("Underweight","Normalweight","Overweight","Obese")
    private lateinit var binding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configChartView()
    }


    private fun configChartView() {
        val pie : Pie = AnyChart.pie()

        val dataPieChart: MutableList<DataEntry> = mutableListOf()

        for (index in bmiList.indices){
            dataPieChart.add(ValueDataEntry(month.elementAt(index),bmiList.elementAt(index)))
        }

        pie.data(dataPieChart)
        pie.title("BMI Overview")
        binding.pieChart.setChart(pie)

    }
}