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
import android.util.Log
import android.widget.Toast
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.itmedicus.randomuser.AlarmReceiver
import com.itmedicus.randomuser.databinding.ActivityAlarmBinding
import java.nio.file.attribute.AclEntry
import java.util.*

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private lateinit var picker : MaterialTimePicker
    private lateinit var calender : Calendar
    private lateinit var alarmManager: AlarmManager
    lateinit var context: Context
    var repReqCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        context = this
        alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        setContentView(binding.root)

        createNotificationChannel()

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

        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,repReqCode,intent,0)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this,"alarm cancel!!",Toast.LENGTH_SHORT).show()
        Log.d("this", "cancel alarm..")
        Log.d("this", "$repReqCode")
    }

    private fun setAlarm(){
        val thuReq : Long = Calendar.getInstance().timeInMillis +1
        repReqCode = thuReq.toInt()
         val intent = Intent(this, AlarmReceiver::class.java)
         val  pendingIntent = PendingIntent.getBroadcast(this,repReqCode,intent,0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
           // Interval 1 day
            24*60*60*1000 ,
            pendingIntent
        )
        Log.d("this", "create alarm")
        Log.d("this", "$repReqCode")
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

                val time = String.format("%02d",picker.hour -12)+ " : "+String.format("%02d",picker.minute) + "PM"
                binding.showTime.text = time

            }else{
                val time = String.format("%02d",picker.hour )+ " : "+String.format("%02d",picker.minute) + "AM"
                binding.showTime.text = time
            }
            calender = Calendar.getInstance()
            calender[Calendar.HOUR_OF_DAY] =  picker.hour
            calender[Calendar.MINUTE] =  picker.minute
            calender[Calendar.SECOND] = 0
            calender[Calendar.MILLISECOND] =  0

        }

    }

    private fun createNotificationChannel() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
          val name : CharSequence = "go for"
          val description = "alarm set"
          val importance = NotificationManager.IMPORTANCE_HIGH
          val channel = NotificationChannel("1000",name,importance)
          channel.description = description
          val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
          notificationManager.createNotificationChannel(channel)
      }
    }

}