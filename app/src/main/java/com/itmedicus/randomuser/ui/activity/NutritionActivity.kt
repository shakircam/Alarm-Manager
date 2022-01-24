package com.itmedicus.randomuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.itmedicus.randomuser.databinding.ActivityHomeBinding
import com.itmedicus.randomuser.databinding.ActivityNutritionBinding

class NutritionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNutritionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNutritionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        receivedData()

    }

    private fun receivedData(){

        val intent = intent
        val title = intent.getStringExtra("title")
        val image = intent.getStringExtra("image")
        val kcal = intent.getIntExtra("kcal",0)
        val protein = intent.getIntExtra("protein",0)
        val fat = intent.getIntExtra("fat",0)
        val carbs = intent.getIntExtra("carbs",0)


        binding.title.text = title
        binding.caloriesNumber.text = kcal.toString()+"kcal"
        binding.carbsNumber.text = carbs.toString()+"g"
        binding.protinNumber.text = protein.toString()+"g"
        binding.fatNumber.text = fat.toString()+"g"
        Log.d("this","$kcal: $protein:$fat:$carbs")

        Glide.with(this)
            .load(image)
            .into(binding.image)

    }

}