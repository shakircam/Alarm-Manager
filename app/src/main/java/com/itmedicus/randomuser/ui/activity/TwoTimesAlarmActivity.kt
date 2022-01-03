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
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.itmedicus.randomuser.AlarmReceiver
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.data.local.UserDatabase
import com.itmedicus.randomuser.databinding.ActivityTwoTimesAlarmBinding
import com.itmedicus.randomuser.model.AlarmTime
import com.itmedicus.randomuser.model.MultipleAlarm
import com.itmedicus.randomuser.ui.fragment.AlarmDialogFragment
import kotlinx.coroutines.launch
import java.util.*

class TwoTimesAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTwoTimesAlarmBinding
    lateinit var context: Context
    lateinit var alarmManager: AlarmManager
    private val CHANNEL_ID = "1000"
    var request_code = 0
    private lateinit var picker: MaterialTimePicker
    private lateinit var calender: Calendar
    private lateinit var sec_calender: Calendar
    private var time = ""
    private var title = ""
    private var second_time = ""
    private var status = ""
    private var calenderTime = 0L
    var alarmList = mutableListOf<AlarmTime>()
    var titleList = arrayOf("Napa", "Ciprocin", "Emcil", "Filmet", "Imotil","Insulin", "Flazil","T Cef","R- Penem","R-Pil")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTwoTimesAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        createNotificationChannel()


        binding.timeTv.setOnClickListener {
            showAlarmDialog()
        }
        binding.firstAlarm.setOnClickListener {
            showTimePicker()
        }

        binding.secondAlarm.setOnClickListener {
            showSecondTimePicker()
        }

        val sharedPreferences = getSharedPreferences("my_sharedPreference",0)
        val editor = sharedPreferences.edit()
        val fk_id : Long = Calendar.getInstance().timeInMillis +2
        editor.putLong("id",fk_id)
        editor.apply()

      /*  val spinner: Spinner = findViewById(R.id.titleTv)
        spinner.onItemSelectedListener = this
        ArrayAdapter(this,android.R.layout.simple_spinner_item,titleList
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }*/

        binding.saveBt.setOnClickListener {

            if (binding.check.isChecked){
                setFirstRepeatingAlarm()
                setSecondRepeatingAlarm()
                status = "Alarm will be repeated"
            }else{
                firstAlarm()
                secondAlarm()
                status = "Alarm will ring once"
            }

            Toast.makeText(this, "alarm set successfully", Toast.LENGTH_SHORT).show()
            title = binding.titleTv.text.toString()
            val addTime= "$time,$second_time"
            val id = sharedPreferences.getLong("id",fk_id)
            val alarmTime = AlarmTime(addTime,id,calenderTime ,title, request_code,status,true)
            alarmList.add(alarmTime)
            val db = UserDatabase.getDatabase(this).userDao()
            lifecycleScope.launch {
                db.insertAlarmTime(alarmTime)
            }
            editor.clear()
            editor.commit()
            val intent = Intent(this,ShowAlarmActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun firstAlarm(){
        calenderTime = calender.timeInMillis
        val thuReq : Long = Calendar.getInstance().timeInMillis +1
        request_code = thuReq.toInt()

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = "okay"
        intent.putExtra("time", time)
        val pendingIntent = PendingIntent.getBroadcast(
            context, request_code, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        Log.d("this", "create alarm: " + Date().toString())
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
            pendingIntent
        )
        val sharedPreferences = getSharedPreferences("my_sharedPreference",0)
        val id = sharedPreferences.getLong("id",-1)
        val multipleAlarm = MultipleAlarm(time,calenderTime,id,"",request_code)
        val db = UserDatabase.getDatabase(this).userDao()
        lifecycleScope.launch {
            db.insertMultipleAlarm(multipleAlarm)
        }
    }

    private fun secondAlarm(){
        calenderTime = calender.timeInMillis
        val thuReq : Long = Calendar.getInstance().timeInMillis +2
        request_code = thuReq.toInt()
        val intent = Intent(context,AlarmReceiver::class.java)
        intent.action = "okay"
        intent.putExtra("time", time)
        val pendingIntent = PendingIntent.getBroadcast(
            context, request_code, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        Log.d("this", "create alarm: " + Date().toString())
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            sec_calender.timeInMillis,
            pendingIntent
        )
        val sharedPreferences = getSharedPreferences("my_sharedPreference",0)
        val id = sharedPreferences.getLong("id",-1)
        val multipleAlarm = MultipleAlarm(second_time,calenderTime,id,"",request_code)
        val db = UserDatabase.getDatabase(this).userDao()
        lifecycleScope.launch {
            db.insertMultipleAlarm(multipleAlarm)
        }
    }

    private fun setFirstRepeatingAlarm(){
        calenderTime = calender.timeInMillis
        val thuReq : Long = Calendar.getInstance().timeInMillis +3
        request_code = thuReq.toInt()
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("time", time)
        val  pendingIntent = PendingIntent.getBroadcast(this,request_code,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
            // Interval one day
            24*60*60*1000,
            pendingIntent
        )
        val sharedPreferences = getSharedPreferences("my_sharedPreference",0)
        val id = sharedPreferences.getLong("id",-1)
        val multipleAlarm = MultipleAlarm(time,calenderTime,id,"",request_code)
        val db = UserDatabase.getDatabase(this).userDao()
        lifecycleScope.launch {
            db.insertMultipleAlarm(multipleAlarm)
        }
        Log.d("this", (calender.timeInMillis ).toString())
        Toast.makeText(this,"alarm set successfully",Toast.LENGTH_SHORT).show()
    }

    private fun setSecondRepeatingAlarm(){
        calenderTime = calender.timeInMillis
        val thuReq : Long = Calendar.getInstance().timeInMillis +4
        request_code = thuReq.toInt()
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "okay"
        intent.putExtra("time", time)
        val  pendingIntent = PendingIntent.getBroadcast(this,request_code,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            sec_calender.timeInMillis,
            // Interval one day
            24*60*60*1000,
            pendingIntent
        )
        val sharedPreferences = getSharedPreferences("my_sharedPreference",0)
        val id = sharedPreferences.getLong("id",-1)
        val multipleAlarm = MultipleAlarm(second_time,calenderTime,id,"",request_code)
        val db = UserDatabase.getDatabase(this).userDao()
        lifecycleScope.launch {
            db.insertMultipleAlarm(multipleAlarm)
        }

        Toast.makeText(this,"alarm set successfully",Toast.LENGTH_SHORT).show()
    }

    private fun showAlarmDialog() {

        val dialogFragment = AlarmDialogFragment()
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog)

        dialogFragment.show(supportFragmentManager, "create_note")
    }

    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12).setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()
        picker.show(supportFragmentManager, "itmedicus")
        picker.addOnPositiveButtonClickListener {
            if (picker.hour > 12) {

                binding.firstAlarm.text =
                    String.format("%02d", picker.hour - 12) + " : " + String.format("%02d", picker.minute) + "PM"
                time = String.format("%02d", picker.hour - 12) + " : " + String.format("%02d", picker.minute) + "PM"
                Log.d("tag", time)

            } else {
                binding.firstAlarm.text =
                    String.format("%02d", picker.hour) + " : " + String.format("%02d", picker.minute) + "AM"
                time = String.format("%02d", picker.hour) + " : " + String.format("%02d", picker.minute) + "AM"
            }
            calender = Calendar.getInstance()
            calender[Calendar.HOUR_OF_DAY] = picker.hour
            calender[Calendar.MINUTE] = picker.minute
            calender[Calendar.SECOND] = 0
            calender[Calendar.MILLISECOND] = 0

        }
    }


    private fun showSecondTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12).setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()
        picker.show(supportFragmentManager, "itmedicus")
        picker.addOnPositiveButtonClickListener {
            if (picker.hour > 12) {

                binding.secondAlarm.text =
                    String.format("%02d", picker.hour - 12) + " : " + String.format(
                        "%02d",
                        picker.minute
                    ) + "PM"
                second_time = String.format("%02d", picker.hour - 12) + " : " + String.format(
                    "%02d",
                    picker.minute
                ) + "PM"
                Log.d("tag", second_time)

            } else {
                binding.secondAlarm.text =
                    String.format("%02d", picker.hour) + " : " + String.format(
                        "%02d",
                        picker.minute
                    ) + "AM"
                second_time = String.format("%02d", picker.hour) + " : " + String.format(
                    "%02d",
                    picker.minute
                ) + "AM"
            }
            sec_calender = Calendar.getInstance()
            sec_calender[Calendar.HOUR_OF_DAY] = picker.hour
            sec_calender[Calendar.MINUTE] = picker.minute
            sec_calender[Calendar.SECOND] = 0
            sec_calender[Calendar.MILLISECOND] = 0

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


}