package com.itmedicus.randomuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.itmedicus.randomuser.databinding.ActivityBmiBinding
import com.itmedicus.randomuser.databinding.ActivityCovidBinding
import com.itmedicus.randomuser.databinding.ActivityHomeBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiBinding

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

        binding.radioFoot.setOnClickListener {
            binding.heightFeet.isVisible = true
            binding.heightInche.isVisible = true
            binding.height.isVisible = true

            binding.cintemeterText.isVisible = false
            binding.cintemeter.isVisible = false
        }


        binding.calculateBt.setOnClickListener {

            val weight = binding.weightEd.text.toString().toDouble()
            val feet = binding.heightFeet.text.toString().toDouble()
            val inch = binding.heightInche.text.toString().toDouble()
           // val cint = binding.cintemeter.text.toString().toDouble()/100

            val feet1 =  ((feet * 30.48) + (inch*2.54))/100
//                val feetToInch = "$feet.$inch".toDouble()
            //    val convertToInch = feetToInch * 12
              //  val meter =  convertToInch * 0.0254


               val newMeter = feet1*feet1
               // val ci = (cint*cint)
                val BMI = BigDecimal(weight/newMeter).setScale(2, RoundingMode.HALF_EVEN).toDouble()
               // val BMI = BigDecimal(weight/ci).setScale(2, RoundingMode.HALF_EVEN).toDouble()


                if (BMI < 18.5) {
                    binding.status.text = "Under Weight"
                } else if (BMI >= 18.5 && BMI < 24.9) {
                    binding.status.text = "Healthy"
                } else if (BMI >= 24.9 && BMI < 30) {
                    binding.status.text = "Overweight"
                } else if (BMI >= 30) {
                    binding.status.text = "Suffering from Obesity"
                }
                binding.resultTx.text = BMI.toString()

        }
    }
}