package com.itmedicus.randomuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        val gender = intent.getStringExtra("gender")
        val email = intent.getStringExtra("email")
        val nat= intent.getStringExtra("nat")
        val phone= intent.getStringExtra("phone")
        val image = intent.getStringExtra("pic")
        val location = intent.getStringExtra("location")
        val state = intent.getStringExtra("state")
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.fabButton.setOnClickListener {
            binding.fabButton.setImageResource(R.drawable.colorstar)

        }

        binding.gender.text = "Gender: "+gender
        binding.email.text = email
        binding.nationality.text = "Nationality: "+nat
        binding.name.text = name
        binding.location.text = location
        binding.phone.text = "Phone: "+phone
        binding.state.text = "State: "+state

        Glide.with(this)
            .load(image)
            .into(binding.imageView)
    }


}