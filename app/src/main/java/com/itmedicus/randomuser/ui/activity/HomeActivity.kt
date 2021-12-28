package com.itmedicus.randomuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import com.denzcoskun.imageslider.models.SlideModel
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.databinding.ActivityDetailsBinding
import com.itmedicus.randomuser.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setTheme(R.style.AppTheme)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(
            "https://images.unsplash.com/photo-1609775436218-78e51e5e61dd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NHx8YWxhcm0lMjBjbG9ja3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=400&q=60"))
        imageList.add(SlideModel(
            "https://images.unsplash.com/photo-1628692945421-21162c93a8a6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjB8fGFsYXJtJTIwY2xvY2t8ZW58MHx8MHx8&auto=format&fit=crop&w=400&q=60"))
        imageList.add(SlideModel(
            "https://images.unsplash.com/photo-1595080472879-bdd162e05586?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTZ8fGFsYXJtJTIwY2xvY2t8ZW58MHx8MHx8&auto=format&fit=crop&w=400&q=60"))
        binding.slideImage.setImageList(imageList)

        binding.card2.setOnClickListener{
            val intent = Intent(this,ListActivity::class.java)
            startActivity(intent)
        }

        binding.card3.setOnClickListener {
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.card4.setOnClickListener {
            val intent = Intent(this,AlarmActivity::class.java)
            startActivity(intent)
        }

        binding.card5.setOnClickListener {
            val intent = Intent(this,ShowAlarmActivity::class.java)
            startActivity(intent)
        }
    }
}