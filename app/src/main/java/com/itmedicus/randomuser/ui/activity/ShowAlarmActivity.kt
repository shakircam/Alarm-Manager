package com.itmedicus.randomuser.ui.activity

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.databinding.ActivityShowAlarmBinding
import java.util.*
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.media.Ringtone

import android.net.Uri
import android.widget.Toast
import kotlin.collections.ArrayList


class ShowAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowAlarmBinding
    lateinit var context: Context
    lateinit var alarmManager: AlarmManager
    private val CHANNEL_ID = "1000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        createNotificationChannel()

        binding.selectTime.setOnClickListener {

            val alarmList = ArrayList<PendingIntent>()
            val time =  binding.timeEd.text.toString().toInt() * 1000


            for ( i in 1..2){
                val intent = Intent(context,Receiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context,i,intent,PendingIntent.FLAG_UPDATE_CURRENT)
                Log.d("this","create alarm: "+Date().toString())
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + time * i,
                    pendingIntent
                )
                alarmList.add(pendingIntent)
            }



           // val secondTime = binding.timeEd2.text.toString().toInt() * 1000
           // setAlarm(time,secondTime)
            Toast.makeText(this,"alarm set successfully",Toast.LENGTH_SHORT).show()
           /* // taking time
            val time =  binding.timeEd.text.toString().toInt() * 1000

            // connect with broadcast receiver by intent
            val intent = Intent(context,Receiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            Log.d("this","create alarm: "+Date().toString())
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + time,
                myTime,
                pendingIntent
            )*/
        }

        binding.updateAlarm.setOnClickListener {

            val time =  binding.timeEd.text.toString().toInt() * 1000
            val intent = Intent(context,Receiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            Log.d("this","update alarm: "+Date().toString())
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + time,
                pendingIntent
            )
        }

        binding.cancelTime.setOnClickListener {

            val intent = Intent(context,Receiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            Log.d("this","cancel alarm: "+Date().toString())
            alarmManager.cancel(pendingIntent)
        }

    }

    private fun setAlarm( time : Int,pk : Int){
       // val time =  binding.timeEd.text.toString().toInt() * 1000
        val intent = Intent(context,Receiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,pk,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + time,
            pendingIntent
        )
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name : CharSequence = "Alarm manager"
            val descriptionText = "Alarm is set now."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    class Receiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("this", "Receive alarm: " + Date().toString())

                // Set the alarm here.
                val vibrator = context?.getSystemService(VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    vibrator.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
                }else{
                    vibrator.vibrate(5000)
                }
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                val r = RingtoneManager.getRingtone(context, notification)
                r.play()

                val intent = Intent(context, ShowAlarmActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

                val builder = NotificationCompat.Builder(context!!, "1000")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Alarm Clock")
                    .setContentText("Alarm manager running")
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.notify(111, builder.build())



        }

    }

}