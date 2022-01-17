package com.itmedicus.randomuser.ui.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.github.anastr.speedviewlib.components.Style
import com.itmedicus.randomuser.data.adapter.BmiAdapter
import com.itmedicus.randomuser.databinding.ActivityBmiBinding
import com.itmedicus.randomuser.model.BmiRecord
import com.itmedicus.randomuser.ui.viewmodel.AlarmViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class BmiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiBinding
    private var BMI = 0.0
    private val list = mutableListOf<BmiRecord>()
    private lateinit var myViewModel: AlarmViewModel
    private var status = ""
    private var height = ""
    private var weight = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        binding.details.setOnClickListener {
            val intent = Intent(this,ChartActivity::class.java)
            startActivity(intent)
        }

        binding.list.setOnClickListener {
            val intent = Intent(this,BmiListActivity::class.java)
            startActivity(intent)
        }


        binding.radioCinte.setOnClickListener {
            binding.heightFeet.isVisible = false
            binding.heightInche.isVisible = false
            binding.height.isVisible = false

            binding.cintemeterText.isVisible = true
            binding.cintemeter.isVisible = true
        }

        binding.radioFeet.setOnClickListener {
            binding.heightFeet.isVisible = true
            binding.heightInche.isVisible = true
            binding.height.isVisible = true

            binding.cintemeterText.isVisible = false
            binding.cintemeter.isVisible = false
        }

        binding.speedView.makeSections(5, Color.CYAN, Style.BUTT)

        // you can change colors to every:
        binding.speedView.sections[0].color = Color.TRANSPARENT
        binding.speedView.sections[1].color = Color.TRANSPARENT
        binding.speedView.sections[2].color = Color.TRANSPARENT
        binding.speedView.sections[3].color = Color.TRANSPARENT
        binding.speedView.sections[4].color = Color.TRANSPARENT


        binding.calculateBt.setOnClickListener {

            val weight = binding.weightEd.text.toString()

            if (binding.radioFeet.isChecked){
                when {

                    TextUtils.isEmpty(binding.heightFeet.text) -> {
                        binding.heightFeet.error = "Feet is required"
                        return@setOnClickListener
                    }
                    TextUtils.isEmpty(binding.heightInche.text) -> {
                        binding.heightInche.error = "Inch is required"
                        return@setOnClickListener
                    }

                    TextUtils.isEmpty(binding.weightEd.text) -> {
                        binding.weightEd.error = "Weight is required"
                        return@setOnClickListener
                    }

                }

                val feet = binding.heightFeet.text.toString().toDouble()
                val inch = binding.heightInche.text.toString().toDouble()


                val convertFeetToInt  = feet.toInt()
                val convertinchToInt  = inch.toInt()
                height = "$convertFeetToInt''$convertinchToInt"

                val feet1 =  ((feet * 30.48) + (inch*2.54))/100
                val newMeter = feet1*feet1
                BMI = BigDecimal(weight.toDouble()/newMeter).setScale(2, RoundingMode.HALF_EVEN).toDouble()

            }


            if (binding.radioCinte.isChecked){


                when {

                    TextUtils.isEmpty(binding.cintemeter.text) -> {
                        binding.cintemeter.error = "Centimeter is required"
                        return@setOnClickListener
                    }

                    TextUtils.isEmpty(binding.weightEd.text) -> {
                        binding.weightEd.error = "Weight is required"
                        return@setOnClickListener
                    }
                }
                 val centi = binding.cintemeter.text.toString().toDouble()/100
                 height = centi.toString()+"cm"
                 val centimeter = (centi*centi)
                 BMI = BigDecimal(weight.toDouble()/centimeter).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            }

            binding.bmi.text = "BMI: "+BMI.toString()
            var bmi = 0f
                if (BMI < 18.5) {
                    bmi = (BMI+8).toFloat()
                    binding.speedView.speedTo(bmi)
                    binding.status.isVisible = true
                    status = "Under Weight"
                    binding.status.text = status
                    binding.status.setTextColor(Color.parseColor("#EEDE4E"))

                    binding.card.text6.setBackgroundColor(Color.parseColor("#FFD600"))


                } else if (BMI >= 18.5 && BMI < 24.9) {
                    bmi = (BMI+20).toFloat()
                    binding.speedView.speedTo(bmi)
                    binding.status.isVisible = true
                    status = "Healthy"
                    binding.status.text = status
                    binding.status.setTextColor(Color.parseColor("#008911"))
                    binding.card.text8.setBackgroundColor(Color.parseColor("#80CBC4"))

                } else if (BMI >= 24.9 && BMI < 30) {
                    bmi = (BMI+30).toFloat()
                    binding.speedView.speedTo(bmi)
                    binding.status.isVisible = true
                    status = "Overweight"
                    binding.status.text = status
                    binding.status.setTextColor(Color.parseColor("#FF7B00"))
                    binding.card.text9.setBackgroundColor(Color.parseColor("#E57373"))

                } else if (BMI >= 30) {
                    bmi = (BMI+45).toFloat()
                    binding.speedView.speedTo(bmi)
                    binding.status.isVisible = true
                    status = "Obese"
                    binding.status.text = status
                    binding.status.setTextColor(Color.parseColor("#FF0000"))
                    binding.card.text10.setBackgroundColor(Color.parseColor("#FF5252"))
                }

            val sdf = SimpleDateFormat("dd MMMM yyyy")
            val currentDate = sdf.format(Date())
            val bmiList = BmiRecord(BMI,status,height,weight.toDouble(),currentDate)
            myViewModel.insertBmiData(bmiList)

        }
    }


}