package com.itmedicus.randomuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.itmedicus.randomuser.data.local.UserDatabase
import com.itmedicus.randomuser.databinding.ActivityBmiBinding
import com.itmedicus.randomuser.databinding.ActivityChartBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ChartActivity : AppCompatActivity() {


    private var bmiList = intArrayOf(0,0,0,5)
    private val bmiStatus = listOf("Underweight","Normalweight","Overweight","Obese")
    private lateinit var binding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addDataTOList()
        configChartView()
    }

    private fun addDataTOList(){
        lifecycleScope.launch(Dispatchers.IO){
            val db = UserDatabase.getDatabase(this@ChartActivity).userDao()
            val bmi = db.getBmiChartResult()
            for ( i in bmi.indices){
                when {
                    i<18.5 -> {

                        bmiList[0] = i
                    }
                    i>18.5 && i<24.9 -> {
                        bmiList[1] = i
                    }
                    i>25.0 &&i<34.9 -> {
                        bmiList[2] = i
                    }
                    i>34.9 -> {
                        bmiList[3] = i

                    }
                }
            }
        }
    }



    private fun configChartView() {
        val pie : Pie = AnyChart.pie()

        val dataPieChart: MutableList<DataEntry> = mutableListOf()

        for (index in bmiList.indices){
            dataPieChart.add(ValueDataEntry(bmiStatus.elementAt(index),bmiList.elementAt(index)))
        }

        pie.data(dataPieChart)
        pie.title("BMI Overview")
        binding.pieChart.setChart(pie)

    }
}