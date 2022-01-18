package com.itmedicus.randomuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    private var chart: AnyChartView? = null
    private var bmiList = mutableListOf<Double>(0.0,0.0,0.0,0.0)
    private val bmiStatus = listOf("Underweight","Normalweight","Overweight","Obese")
    private lateinit var binding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        chart = binding.pieChart
        setContentView(binding.root)
        addDataTOList()
        configChartView()
    }

    private fun addDataTOList(){
        lifecycleScope.launch(Dispatchers.IO){
            val db = UserDatabase.getDatabase(this@ChartActivity).userDao()
            val bmi = db.getBmiChartResult()
            for ( i in bmi.indices){
                Log.d("this", bmi[i].bmiScore.toString())
                when {
                    bmi[i].bmiScore!! <18.5 -> {

                        bmiList[0] += 10.0

                    }
                    bmi[i].bmiScore!!>18.5 && bmi[i].bmiScore!!<24.9 -> {
                        bmiList[1] += 10.0
                    }
                    bmi[i].bmiScore!!>25.0 &&bmi[i].bmiScore!!<34.9 -> {
                        bmiList[2] += 10.0
                    }
                    bmi[i].bmiScore!!>34.9 -> {
                        bmiList[3] += 10.0
                    }
                }
                Log.d("this",bmiList.toString())
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
        pie.title("All Users Bmi Overview")
        chart!!.setChart(pie)

    }
}