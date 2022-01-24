package com.itmedicus.randomuser.ui.activity

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.itmedicus.randomuser.AlarmReceiver
import com.itmedicus.randomuser.databinding.ActivityAlarmTestBinding
import com.itmedicus.randomuser.databinding.ActivityShowAlarmBinding

class AlarmTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmTestBinding
    lateinit var alarmManager: AlarmManager
    private val CHANNEL_ID = "1000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        createNotificationChannel()


        binding.setexact.setOnClickListener {
            setExactAlarm()
        }

        binding.setexactandAllow.setOnClickListener {
            setExactAndAllowWhileIdleAlarm()
        }

        binding.setAllow.setOnClickListener {
            setAndAllowWhileIdleAlarm()
        }

        binding.setAlarm.setOnClickListener {
            setAlarmClockAlarm()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Alarm manager"
            val descriptionText = "Alarm is set now."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setExactAlarm(){

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "okay"
        intent.putExtra("time", "time")
        val  pendingIntent = PendingIntent.getBroadcast(this,1,intent, 0)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 360000,
            pendingIntent
        )
        Toast.makeText(this,"click",Toast.LENGTH_SHORT).show()
    }

    private fun setExactAndAllowWhileIdleAlarm(){

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "okay"
        intent.putExtra("time", "time")
        val  pendingIntent = PendingIntent.getBroadcast(this,3,intent, 0)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 360000,
            pendingIntent
        )
        Toast.makeText(this,"click",Toast.LENGTH_SHORT).show()
    }

    private fun setAndAllowWhileIdleAlarm(){

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "okay"
        intent.putExtra("time", "time")
        val  pendingIntent = PendingIntent.getBroadcast(this,4,intent, 0)
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 360000,
            pendingIntent
        )
        Toast.makeText(this,"click",Toast.LENGTH_SHORT).show()
    }

    private fun setAlarmClockAlarm(){

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "okay"
        intent.putExtra("time", "time")
        val  pendingIntent = PendingIntent.getBroadcast(this,5,intent, 0)
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(System.currentTimeMillis() + 360000,pendingIntent),pendingIntent)
        Toast.makeText(this,"click",Toast.LENGTH_SHORT).show()
    }

}