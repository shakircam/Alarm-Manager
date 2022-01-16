package com.itmedicus.randomuser.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import com.github.anastr.speedviewlib.components.Style
import com.itmedicus.randomuser.databinding.ActivityBmiBinding
import com.itmedicus.randomuser.databinding.ActivityCovidBinding
import com.itmedicus.randomuser.databinding.ActivityHomeBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiBinding
    private var BMI = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            val weight = binding.weightEd.text.toString().toDouble()
            val feet = binding.heightFeet.text.toString().toDouble()
            val inch = binding.heightInche.text.toString().toDouble()



            if (binding.radioFeet.isChecked){

                val feet1 =  ((feet * 30.48) + (inch*2.54))/100
                val newMeter = feet1*feet1
                 BMI = BigDecimal(weight/newMeter).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            }

            if (binding.radioCinte.isChecked){
                 val centi = binding.cintemeter.text.toString().toDouble()/100
                 val centimeter = (centi*centi)
                 BMI = BigDecimal(weight/centimeter).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            }



            binding.bmi.text = "BMI: "+BMI.toString()
            var bmi = 0f
                if (BMI < 18.5) {
                    bmi = (BMI+8).toFloat()
                    binding.speedView.speedTo(bmi)
                    binding.status.isVisible = true
                    binding.status.text = "Under Weight"
                    binding.status.setTextColor(Color.parseColor("#EEDE4E"))

                } else if (BMI >= 18.5 && BMI < 24.9) {
                    bmi = (BMI+20).toFloat()
                    binding.speedView.speedTo(bmi)
                    binding.status.isVisible = true
                    binding.status.text = "Healthy".apply {
                        binding.status.setTextColor(Color.parseColor("#008911"))
                    }


                } else if (BMI >= 24.9 && BMI < 30) {
                    bmi = (BMI+30).toFloat()
                    binding.speedView.speedTo(bmi)
                    binding.status.isVisible = true
                    binding.status.text = "Overweight"
                    binding.status.setTextColor(Color.parseColor("#FF7B00"))

                } else if (BMI >= 30) {
                    bmi = (BMI+45).toFloat()
                    binding.speedView.speedTo(bmi)
                    binding.status.isVisible = true
                    binding.status.text = "Obese"
                    binding.status.setTextColor(Color.parseColor("#FF0000"))
                }
        }
    }
}