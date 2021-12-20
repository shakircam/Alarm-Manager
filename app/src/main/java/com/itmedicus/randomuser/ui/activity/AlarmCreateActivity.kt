package com.itmedicus.randomuser.ui.activity

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.data.local.UserDatabase
import com.itmedicus.randomuser.databinding.ActivityAlarmBinding
import com.itmedicus.randomuser.databinding.ActivityAlarmCreateBinding
import com.itmedicus.randomuser.databinding.ActivityShowAlarmBinding
import com.itmedicus.randomuser.model.AlarmTime
import com.itmedicus.randomuser.ui.fragment.AlarmDialogFragment
import com.itmedicus.randomuser.ui.fragment.WeekDialogFragment
import kotlinx.coroutines.launch
import java.util.*

class AlarmCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmCreateBinding
    lateinit var context: Context
    lateinit var alarmManager: AlarmManager
    private val CHANNEL_ID = "1000"
    var request_code= 0
    private lateinit var picker : MaterialTimePicker
    private lateinit var calender : Calendar
    private var time = ""
    private var status = ""
    var alarmList = mutableListOf<AlarmTime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        binding.timeTv.setOnClickListener {
            showAlarmDialog()
        }

        binding.weekTv.setOnClickListener {
            showAlarmWeekDialog()
        }

        createNotificationChannel()

        binding.showTime.setOnClickListener {
            showTimePicker()
        }


            val intent = intent
            val sat = intent.getStringExtra("saturday")
            val sun = intent.getStringExtra("sunday")
            val mon = intent.getStringExtra("monday")
            val tue = intent.getStringExtra("tuesday")
            val wed = intent.getStringExtra("wednesday")
            val thu = intent.getStringExtra("thursday")
            val fri = intent.getStringExtra("friday")
            Log.d("day",sat.toString())


        binding.saveBt.setOnClickListener {
            //setRepeatingAlarm(Calendar.MONDAY)
            if (sat == "1"){
                status = "Saturday"
                setRepeatingAlarm(Calendar.SATURDAY)
            }
            if (sun == "2"){
                status = "Sunday"
                setRepeatingAlarm(Calendar.SUNDAY)
            }
            if (mon == "3"){
                status = "Monday"
                setRepeatingAlarm(Calendar.MONDAY)
            }
            if (tue == "4"){
                status = "Tuesday"
                setRepeatingAlarm(Calendar.TUESDAY)
            }
            if (wed == "5"){
                status = "Wednesday"
                setRepeatingAlarm(Calendar.WEDNESDAY)
            }
            if (thu == "6"){
                status = "Thursday"
                setRepeatingAlarm(Calendar.THURSDAY)
            }
            if (fri == "7"){
                status = "Friday"
                setRepeatingAlarm(Calendar.FRIDAY)
            }

            if (binding.check.isChecked){
                repeatingAlarm()
                status = "The Alarm will continue"

            }else{
                setSingleAlarm()
                status = ""
            }

            Toast.makeText(this,"alarm set successfully", Toast.LENGTH_SHORT).show()
            val title = binding.titleTv.text.toString()
            val alarmTime= AlarmTime(time,title,0,status)
            alarmList.add(alarmTime)
            val db = UserDatabase.getDatabase(this).userDao()
            lifecycleScope.launch {
                db.insertAlarmTime(alarmTime)
            }
        }

    }



    private fun setSingleAlarm(){
          val intent = Intent(context, AlarmReceiver::class.java)
           val pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
           Log.d("this","create alarm: "+Date().toString())
           alarmManager.set(
               AlarmManager.RTC_WAKEUP,
               calender.timeInMillis ,
               pendingIntent
           )
    }

    private fun setRepeatingAlarm(dayOfWeek: Int){

        calender = Calendar.getInstance()
        calender.set(Calendar.DAY_OF_WEEK,dayOfWeek)

        if(calender.timeInMillis < System.currentTimeMillis()) {
            calender.add(Calendar.DAY_OF_YEAR, 7)
        }

        val intent = Intent(this,AlarmReceiver::class.java)
        val  pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
             //AlarmManager.INTERVAL_DAY * 7,
            24*60*60*1000 * 7,
            pendingIntent
        )
        Log.d("this", (calender.timeInMillis ).toString())
        Toast.makeText(this,"alarm set successfully",Toast.LENGTH_SHORT).show()
    }


    private fun repeatingAlarm(){
        val intent = Intent(this, AlarmReceiver::class.java)
        val  pendingIntent = PendingIntent.getBroadcast(this,4,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
            // Interval one day
            24*60*60*1000,
            pendingIntent
        )
        Log.d("this", (calender.timeInMillis ).toString())
        Toast.makeText(this,"alarm set successfully",Toast.LENGTH_SHORT).show()
    }

    private fun showAlarmDialog() {

        val dialogFragment = AlarmDialogFragment()
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog)

        dialogFragment.show(supportFragmentManager, "create_note")
    }

    private fun showAlarmWeekDialog() {

        val dialogFragment = WeekDialogFragment()
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog)

        dialogFragment.show(supportFragmentManager, "create_alarm")
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

                binding.showTime.text = String.format("%02d",picker.hour -12)+ " : "+String.format("%02d",picker.minute) + "PM"
                 time = String.format("%02d",picker.hour -12)+ " : "+String.format("%02d",picker.minute) + "PM"
            }else{
                binding.showTime.text =  String.format("%02d",picker.hour )+ " : "+String.format("%02d",picker.minute) + "AM"
                time = String.format("%02d",picker.hour )+ " : "+String.format("%02d",picker.minute) + "AM"
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

    class AlarmReceiver : BroadcastReceiver(){
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
                .setContentText("Time to take your medicine")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(111, builder.build())

        }
    }


}