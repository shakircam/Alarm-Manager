package com.itmedicus.randomuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.data.adapter.ItemClickListener
import com.itmedicus.randomuser.databinding.ActivityDetailsBinding
import com.itmedicus.randomuser.databinding.ActivityListBinding
import com.itmedicus.randomuser.model.Result

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val intent = intent

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("gender")
        val gender = intent.getStringExtra("email")
        val nat= intent.getStringExtra("nat")
        val phone= intent.getStringExtra("phone")
        val image = intent.getStringExtra("pic")
        val location = intent.getStringExtra("location")
        val state = intent.getStringExtra("state")
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        binding.arrowDown.setOnClickListener {
            binding.extraText.isVisible = true
            binding.extraText.animation = bottomAnim
            binding.arrowUp.animation = bottomAnim
            binding.arrowDown.isVisible = false
            binding.arrowUp.isVisible = true
        }
        binding.arrowUp.setOnClickListener {

            binding.extraText.isVisible = false
            binding.arrowDown.isVisible = true
            binding.arrowUp.isVisible = false
        }


        binding.gender.text = gender
        binding.frontLayout.email.text = email
       // binding.email.text = email
        binding.nationality.text = nat
        binding.frontLayout.name.text = name
       // binding.name.text = name
        binding.frontLayout.location.text = location
       // binding.location.text = location
        binding.phone.text = phone
        binding.state.text = state

        Glide.with(this)
            .load(image)
            .into(binding.frontLayout.imageView)
           // .into(binding.imageView)
    }


}