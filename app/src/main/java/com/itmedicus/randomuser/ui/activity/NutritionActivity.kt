package com.itmedicus.randomuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itmedicus.randomuser.databinding.ActivityHomeBinding
import com.itmedicus.randomuser.databinding.ActivityNutritionBinding

class NutritionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNutritionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNutritionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}