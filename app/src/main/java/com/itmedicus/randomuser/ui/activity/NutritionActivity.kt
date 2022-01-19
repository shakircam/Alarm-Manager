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
        val kcal = intent.getStringExtra("kcal")
        val protein = intent.getStringExtra("protein")
        val fat = intent.getStringExtra("fat")
        val carbs = intent.getStringExtra("carbs")


        binding.title.text = title
        binding.caloriesNumber.text = kcal.toString()
        binding.carbsNumber.text = carbs.toString()
        binding.protinNumber.text = protein.toString()
        binding.fatNumber.text = fat.toString()
        Log.d("this","$kcal: $protein:$fat:$carbs")

        Glide.with(this)
            .load(image)
            .into(binding.image)

    }

}