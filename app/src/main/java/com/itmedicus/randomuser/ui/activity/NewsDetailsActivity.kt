package com.itmedicus.randomuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.itmedicus.randomuser.databinding.ActivityNewsBinding
import com.itmedicus.randomuser.databinding.ActivityNewsDetailsBinding

class NewsDetailsActivity : AppCompatActivity() {

    companion object{
        const val TAG = "NewsDetailsActivity"
    }
    private lateinit var binding: ActivityNewsDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        val title = intent.getStringExtra("title")
        val excerpt = intent.getStringExtra("excerpt")
        val image = intent.getStringExtra("image")

        binding.title.text = title
        binding.description.text = excerpt
        Glide.with(this)
            .load(image)
            .into(binding.image)
    }
}