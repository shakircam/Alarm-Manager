package com.itmedicus.randomuser.ui.activity

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.itmedicus.randomuser.databinding.ActivityAlarmBinding
import java.nio.file.attribute.AclEntry
import java.util.*

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private lateinit var picker : MaterialTimePicker
    private lateinit var calender : Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        notificationChannel()

        binding.selectTime.setOnClickListener {
            showTimePicker()
        }
        binding.setAlarm.setOnClickListener {
            setAlarm()
        }
        binding.cancelTime.setOnClickListener {
            cancelAlarm()
        }
    }

    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this,AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this,"alarm cancel!!",Toast.LENGTH_SHORT).show()
    }

    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this,AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,calender.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent
        )
        Toast.makeText(this,"alarm set successfully",Toast.LENGTH_SHORT).show()
    }

    private fun showTimePicker() {
       picker = MaterialTimePicker.Builder()
           .setTimeFormat(TimeFormat.CLOCK_12H)
           .setHour(12).setMinute(0)
           .setTitleText("Select Alarm Time")
           .build()
        picker.show(supportFragmentManager,"itmedicus")
        picker.addOnPositiveButtonClickListener {
            if (picker.hour > 12){
               binding.timeView.text =
                   String.format("%02d",picker.hour -12)+ " : "+String.format("%02d",picker.minute) + "PM"

            }else{
                String.format("%02d",picker.hour )+ " : "+String.format("%02d",picker.minute) + "AM"
            }
            calender = Calendar.getInstance()
            calender[Calendar.HOUR_OF_DAY]=  picker.hour
            calender[Calendar.MINUTE]=  picker.minute
            calender[Calendar.SECOND]= 0
            calender[Calendar.MILLISECOND]=  0

        }

    }

    private fun notificationChannel() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
          val name : CharSequence = "go for"
          val description = "alarm set"
          val importance = NotificationManager.IMPORTANCE_HIGH
          val channel = NotificationChannel("itmedicuss",name,importance)
          channel.description = description
          val notificationManager = getSystemService(NotificationManager::class.java)
          notificationManager.createNotificationChannel(channel)
      }
    }
}