package com.itmedicus.randomuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.databinding.ActivityNotificationShowBinding

class NotificationShowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationShowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.animation = AnimationUtils.loadAnimation(this, R.anim.shake_image)

        binding.cancel.setOnClickListener {
            Intent(this,ShowAlarmActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }
}