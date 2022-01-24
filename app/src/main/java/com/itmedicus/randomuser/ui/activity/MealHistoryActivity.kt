package com.itmedicus.randomuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itmedicus.randomuser.databinding.ActivityMealHistoryBinding
import com.itmedicus.randomuser.databinding.ActivityNutritionBinding

class MealHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}