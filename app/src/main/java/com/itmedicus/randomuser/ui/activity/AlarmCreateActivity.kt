package com.itmedicus.randomuser.ui.activity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings.System.getString
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.itmedicus.randomuser.AlarmReceiver
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.data.local.UserDatabase
import com.itmedicus.randomuser.databinding.ActivityAlarmCreateBinding
import com.itmedicus.randomuser.model.AlarmTime
import com.itmedicus.randomuser.model.MultipleAlarm
import com.itmedicus.randomuser.model.TwoTimesAlarm
import com.itmedicus.randomuser.ui.fragment.AlarmDialogFragment
import com.itmedicus.randomuser.ui.fragment.WeekDialogFragment
import kotlinx.coroutines.launch
import java.util.*
import java.util.UUID




class AlarmCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmCreateBinding
    lateinit var context: Context
    lateinit var alarmManager: AlarmManager
    private val CHANNEL_ID = "1000"
    private val NOTIFICATIONID = 111
    var repReqCode = 0
    var flag = 0
    var flag1 = 0
    private lateinit var picker : MaterialTimePicker
    private lateinit var calender : Calendar
    private lateinit var sec_calender  : Calendar
    private var time = ""
    private var second_time = ""
    private var status = ""
    private var title = ""
    private var calenderTime = 0L
    private var secCalenderTime = 0L
    private var sat = ""
    private var sun = ""
    private var mon = ""
    private var tue = ""
    private var wed = ""
    private var thu = ""
    private var fri = ""
    var alarmList = mutableListOf<AlarmTime>()
    private val stringBuilder = StringBuilder()
    private val stringBuffer = StringBuilder()
    private var titleList = arrayOf("Pill", "Inhaler", "Injection")
    var counter = 1
    var number = 1
    var pills = ""
    var intervalnumber = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        context = this
        alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.timeTv.setOnClickListener {
            customDialog()

        }
        binding.everyDay.setOnClickListener {
            binding.weekTv2.isVisible = false
            binding.intervalDays.isVisible = false
        }

        binding.specificDay.setOnClickListener {
            openDialog()
            binding.weekTv2.isVisible = true
            binding.intervalDays.isVisible = false
        }

        binding.intervalDay.setOnClickListener {
            intervalDialog()
            binding.intervalDays.isVisible = true
            binding.weekTv2.isVisible = false
        }

        binding.plusBt.setOnClickListener {
            if (counter>=1){
                counter ++
            }
            number = counter * 1
            binding.number.text = number.toString()
        }

        binding.minusBt.setOnClickListener {
            if (counter>1){
                counter--
            }
            number = counter / 1
            binding.number.text = number.toString()
        }

        createNotificationChannel()

        binding.showTime.setOnClickListener {
            showTimePicker()
        }

        binding.showTime2.setOnClickListener {
            showSecondTimePicker()
        }

        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,titleList)
        binding.spinnerText.setAdapter(arrayAdapter)
        binding.spinnerText.threshold = 1

        // Apply SharedPreferences for storing key
        val sharedPreferences = getSharedPreferences("my_sharedPreference", 0)
        val editor = sharedPreferences.edit()
        val fk_id: Long = Calendar.getInstance().timeInMillis + 2
        editor.putLong("id", fk_id)
        editor.apply()


        binding.saveBt.setOnClickListener {

            title = binding.titleTv.text.toString()
            when {
                TextUtils.isEmpty(title) -> {
                    binding.titleTv.error = "Alarm Title is required"
                    return@setOnClickListener
                }
            }

            if (binding.intervalDay.isChecked){
                repeatingAlarm(intervalnumber.toLong())
                status = "Alarm will repeat after $intervalnumber days"
                stringBuilder.append(status + "")
                Log.d("this", "interval alarm")

            }else{

                if (binding.specificDay.isChecked) {

                    if (sat == "1") {
                        setRepeatingSaturdayAlarm()
                        status = "Sat"
                        stringBuilder.append(status + " ")
                        Log.d("tag", "saturday")
                    }
                    if (sun == "2") {
                        setRepeatingSundayAlarm()
                        status = "Sun"
                        stringBuilder.append(status + " ")
                        Log.d("tag", "sunday")
                    }
                    if (mon == "3") {
                        setRepeatingMondayAlarm()
                        status = "Mon"
                        stringBuilder.append(status + " ")
                        Log.d("tag", "monday")
                    }
                    if (tue == "4") {
                        setRepeatingTuesdayAlarm()
                        status = "Tue"
                        stringBuilder.append(" $status")
                        Log.d("tag", "tuesday")
                    }
                    if (wed == "5") {
                        setRepeatingWednesdayAlarm()
                        status = "Wed"
                        stringBuilder.append(status + " ")
                        Log.d("tag", "wednesday")
                    }
                    if (thu == "6") {
                        setRepeatingThursdayAlarm()
                        status = "Thu"
                        stringBuilder.append(status + " ")
                        Log.d("tag", "thursday")
                    }
                    if (fri == "7") {
                        setRepeatingFridayAlarm()
                        status = "Fri"
                        stringBuilder.append(status)
                        Log.d("tag", "friday")
                    }
                } else {
                    if (flag1 == 10){
                        Log.d("this","two alarm")
                        repeatingAlarm()
                        setSecondRepeatingAlarm()
                        time = "$time,$second_time"
                        Log.d("this",time)
                        status = "Every Day"
                        stringBuilder.append(status + "")
                    }else{
                        repeatingAlarm()
                        status = "Every Day"
                        stringBuilder.append(status + "")
                        Log.d("this", "repeating alarm")
                    }
                }
            }


            Toast.makeText(this, "alarm set successfully", Toast.LENGTH_SHORT).show()
             calenderTime = calender.timeInMillis
             pills = binding.spinnerText.text.toString()

            val id = sharedPreferences.getLong("id", fk_id)
            Log.d("thi",pills)
            val alarmTime = AlarmTime(time,pills,number, id, calenderTime, title, repReqCode, stringBuilder.toString(), true)
            alarmList.add(alarmTime)
            val db = UserDatabase.getDatabase(this).userDao()
            lifecycleScope.launch {
                db.insertAlarmTime(alarmTime)
            }
            editor.clear()
            editor.commit()
            val intent = Intent(this, ShowAlarmActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

        private fun repeatingAlarm(day:Long) {
            val  calenderTime = calender.timeInMillis
            val thuReq: Long = Calendar.getInstance().timeInMillis + 1
            repReqCode = thuReq.toInt()

            val intent = Intent(this, AlarmReceiver::class.java)
            intent.action = "okay"
            intent.putExtra("time", time)
           // intent.putExtra("notification_id",NOTIFICATIONID)
            val pendingIntent = PendingIntent.getBroadcast(this, repReqCode, intent, 0)

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calenderTime,
                // Interval one day
                day*(24*60*60*1000),
                pendingIntent
            )

            Log.d("thiss", (day*(24*60*60*1000)).toString())
            Log.d("this", "When alarm will ring::: $calenderTime")
            Log.d("this", "Current time when alarm create::  " + System.currentTimeMillis().toString())
            Log.d("this", "alarm create :::request code::$repReqCode")

            Toast.makeText(this, "alarm set successfully", Toast.LENGTH_SHORT).show()
        }

    private fun repeatingAlarm() {
        calenderTime = calender.timeInMillis
        val thuReq: Long = Calendar.getInstance().timeInMillis + 1
        repReqCode = thuReq.toInt()

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "okay"
        intent.putExtra("time", time)

        val pendingIntent = PendingIntent.getBroadcast(this, repReqCode, intent, 0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calenderTime,
            // Interval one day
            24*60*60*1000,
            pendingIntent
        )

        Log.d("this", "When alarm will ring::: $calenderTime")
        Log.d("this", "Current time when alarm create::  " + System.currentTimeMillis().toString())
        Log.d("this", "alarm create :::request code::$repReqCode")

        Toast.makeText(this, "alarm set successfully", Toast.LENGTH_SHORT).show()
    }

    private fun setSecondRepeatingAlarm(){

        secCalenderTime = sec_calender.timeInMillis

        val thuReq : Long = Calendar.getInstance().timeInMillis +8
        repReqCode = thuReq.toInt()
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "okay"
        intent.putExtra("time", time)
        val  pendingIntent = PendingIntent.getBroadcast(this,thuReq.toInt(),intent,PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            secCalenderTime,
            // Interval one day
            24*60*60*1000,
            pendingIntent
        )
        val sharedPreferences = getSharedPreferences("my_sharedPreference",0)
        val id = sharedPreferences.getLong("id",-1)
        val twoTimesAlarm = TwoTimesAlarm(second_time,secCalenderTime,id,"everyday",thuReq.toInt())
        val db = UserDatabase.getDatabase(this).userDao()
        lifecycleScope.launch {
            db.insertTwoTimesAlarm(twoTimesAlarm)
        }
        Toast.makeText(this,"alarm set successfully",Toast.LENGTH_SHORT).show()
    }


        // saturday
        private fun setRepeatingSaturdayAlarm() {

            val thuReq: Long = Calendar.getInstance().timeInMillis + 1
            repReqCode = thuReq.toInt()

            calender = Calendar.getInstance()
            calender.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
            val day = "Saturday"
            if (calender.timeInMillis < System.currentTimeMillis()) {
                calender.add(Calendar.DAY_OF_YEAR, 7)
            }
            calenderTime = calender.timeInMillis
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.action = "okay"
            intent.putExtra("time", time)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                repReqCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calender.timeInMillis,
                //AlarmManager.INTERVAL_DAY * 7,
                24 * 60 * 60 * 1000 * 7,
                pendingIntent
            )
            Log.d("this", "alarm created")
            val sharedPreferences = getSharedPreferences("my_sharedPreference", 0)
            val id = sharedPreferences.getLong("id", -1)

            val multipleAlarm = MultipleAlarm(time, calenderTime, id, day, repReqCode)
            val db = UserDatabase.getDatabase(this).userDao()
            lifecycleScope.launch {
                db.insertMultipleAlarm(multipleAlarm)
            }
            Log.d("code", repReqCode.toString())
            Toast.makeText(this, "alarm set successfully", Toast.LENGTH_SHORT).show()
        }

        // sunday
        private fun setRepeatingSundayAlarm() {

            val thuReq: Long = Calendar.getInstance().timeInMillis + 2
            repReqCode = thuReq.toInt()

            calender = Calendar.getInstance()
            calender.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            val day = "Sunday"
            if (calender.timeInMillis < System.currentTimeMillis()) {
                calender.add(Calendar.DAY_OF_YEAR, 7)
            }
            calenderTime = calender.timeInMillis
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.action = "okay"
            intent.putExtra("time", time)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                repReqCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calender.timeInMillis,
                //AlarmManager.INTERVAL_DAY * 7,
                24 * 60 * 60 * 1000 * 7,
                pendingIntent
            )
            Log.d("this", "alarm created")
            val sharedPreferences = getSharedPreferences("my_sharedPreference", 0)
            val id = sharedPreferences.getLong("id", -1)
            val multipleAlarm = MultipleAlarm(time, calenderTime, id, day, repReqCode)
            val db = UserDatabase.getDatabase(this).userDao()
            lifecycleScope.launch {
                db.insertMultipleAlarm(multipleAlarm)
            }
            Log.d("code", repReqCode.toString())
            Toast.makeText(this, "alarm set successfully", Toast.LENGTH_SHORT).show()
        }

        // monday
        private fun setRepeatingMondayAlarm() {

            val thuReq: Long = Calendar.getInstance().timeInMillis + 3
            repReqCode = thuReq.toInt()
            calender = Calendar.getInstance()
            calender.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            val day = "Monday"
            if (calender.timeInMillis < System.currentTimeMillis()) {
                calender.add(Calendar.DAY_OF_YEAR, 7)
            }
            calenderTime = calender.timeInMillis
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.action = "okay"
            intent.putExtra("time", time)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                repReqCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calender.timeInMillis,
                //AlarmManager.INTERVAL_DAY * 7,
                24 * 60 * 60 * 1000 * 7,
                pendingIntent
            )
            val sharedPreferences = getSharedPreferences("my_sharedPreference", 0)
            val id = sharedPreferences.getLong("id", -1)
            val multipleAlarm = MultipleAlarm(time, calenderTime, id, day, repReqCode)
            val db = UserDatabase.getDatabase(this).userDao()
            lifecycleScope.launch {
                db.insertMultipleAlarm(multipleAlarm)
            }
            Log.d("code", repReqCode.toString())
            Toast.makeText(this, "alarm set successfully", Toast.LENGTH_SHORT).show()
        }

        // tuesday
        private fun setRepeatingTuesdayAlarm() {

            val thuReq: Long = Calendar.getInstance().timeInMillis + 4
            repReqCode = thuReq.toInt()
            calender = Calendar.getInstance()
            calender.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
            val day = "Tuesday"
            if (calender.timeInMillis < System.currentTimeMillis()) {
                calender.add(Calendar.DAY_OF_YEAR, 7)
            }
            calenderTime = calender.timeInMillis
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.action = "okay"
            intent.putExtra("time", time)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                repReqCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calender.timeInMillis,
                //AlarmManager.INTERVAL_DAY * 7,
                24 * 60 * 60 * 1000 * 7,
                pendingIntent
            )
            val sharedPreferences = getSharedPreferences("my_sharedPreference", 0)
            val id = sharedPreferences.getLong("id", -1)
            val multipleAlarm = MultipleAlarm(time, calenderTime, id, day, repReqCode)
            val db = UserDatabase.getDatabase(this).userDao()
            lifecycleScope.launch {
                db.insertMultipleAlarm(multipleAlarm)
            }
            Log.d("code", repReqCode.toString())
            Log.d("tag", "Tuesday alarm create")
            Toast.makeText(this, "alarm set successfully", Toast.LENGTH_SHORT).show()
        }

        // wednesday
        private fun setRepeatingWednesdayAlarm() {

            val thuReq: Long = Calendar.getInstance().timeInMillis + 5
            repReqCode = thuReq.toInt()
            calenderTime = calender.timeInMillis
            calender = Calendar.getInstance()
            calender.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
            val day = "Wednesday"
            if (calender.timeInMillis < System.currentTimeMillis()) {
                calender.add(Calendar.DAY_OF_YEAR, 7)
            }
            calenderTime = calender.timeInMillis
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.action = "okay"
            intent.putExtra("time", time)
            val pendingIntent = PendingIntent.getBroadcast(this, repReqCode, intent, 0)

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calender.timeInMillis,
                24 * 60 * 60 * 1000 * 7,
                pendingIntent
            )
            val sharedPreferences = getSharedPreferences("my_sharedPreference", 0)
            val id = sharedPreferences.getLong("id", -1)
            val multipleAlarm = MultipleAlarm(time, calenderTime, id, day, repReqCode)
            val db = UserDatabase.getDatabase(this).userDao()
            lifecycleScope.launch {
                db.insertMultipleAlarm(multipleAlarm)
            }
            Log.d("code", repReqCode.toString())
            Toast.makeText(this, "alarm set successfully", Toast.LENGTH_SHORT).show()
        }

        // thursday
        private fun setRepeatingThursdayAlarm() {

            val thuReq: Long = Calendar.getInstance().timeInMillis + 6
            repReqCode = thuReq.toInt()

            calender = Calendar.getInstance()
            calender.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
            val day = "Thursday"
            if (calender.timeInMillis < System.currentTimeMillis()) {
                calender.add(Calendar.DAY_OF_YEAR, 7)
            }
            calenderTime = calender.timeInMillis
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.action = "okay"
            intent.putExtra("time", time)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                repReqCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calender.timeInMillis,
                //AlarmManager.INTERVAL_DAY * 7,
                24 * 60 * 60 * 1000 * 7,
                pendingIntent
            )
            val sharedPreferences = getSharedPreferences("my_sharedPreference", 0)
            val id = sharedPreferences.getLong("id", -1)
            val multipleAlarm = MultipleAlarm(time, calenderTime, id, day, repReqCode)
            val db = UserDatabase.getDatabase(this).userDao()
            lifecycleScope.launch {
                db.insertMultipleAlarm(multipleAlarm)
            }
            Log.d("code", repReqCode.toString())
            Log.d("tag", "Thursday alarm create")
            Toast.makeText(this, "alarm set successfully", Toast.LENGTH_SHORT).show()
        }

        // friday
        private fun setRepeatingFridayAlarm() {

            val thuReq: Long = Calendar.getInstance().timeInMillis + 7
            repReqCode = thuReq.toInt()

            calender = Calendar.getInstance()
            calender.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
            val day = "Friday"
            if (calender.timeInMillis < System.currentTimeMillis()) {
                calender.add(Calendar.DAY_OF_YEAR, 7)
            }
            calenderTime = calender.timeInMillis
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.action = "okay"
            intent.putExtra("time", time)
            val pendingIntent = PendingIntent.getBroadcast(this, repReqCode, intent, 0)

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calender.timeInMillis,
                24 * 60 * 60 * 1000 * 7,
                pendingIntent
            )
            val sharedPreferences = getSharedPreferences("my_sharedPreference", 0)
            val id = sharedPreferences.getLong("id", -1)
            val multipleAlarm = MultipleAlarm(time, calenderTime, id, day, repReqCode)
            val db = UserDatabase.getDatabase(this).userDao()
            lifecycleScope.launch {
                db.insertMultipleAlarm(multipleAlarm)
            }
            Log.d("code", repReqCode.toString())
            Toast.makeText(this, "alarm set successfully", Toast.LENGTH_SHORT).show()
        }


        private fun showTimePicker() {
            picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12).setMinute(0)
                .setTitleText("Select Alarm Time")
                .build()
            picker.show(supportFragmentManager, "itmedicus")

            picker.addOnPositiveButtonClickListener {
                if (picker.hour == 12 && picker.minute>0) {

                    binding.showTime.text =
                        String.format("%02d", picker.hour) + " : " + String.format(
                            "%02d",
                            picker.minute
                        ) + "PM"
                    time = String.format("%02d", picker.hour) + " : " + String.format(
                        "%02d",
                        picker.minute
                    ) + "PM"
                } else  if (picker.hour > 12) {

                    binding.showTime.text =
                        String.format("%02d", picker.hour - 12) + " : " + String.format(
                            "%02d",
                            picker.minute
                        ) + "PM"
                    time = String.format("%02d", picker.hour - 12) + " : " + String.format(
                        "%02d",
                        picker.minute
                    ) + "PM"
                } else {
                    binding.showTime.text =
                        String.format("%02d", picker.hour) + " : " + String.format(
                            "%02d",
                            picker.minute
                        ) + "AM"
                    time = String.format("%02d", picker.hour) + " : " + String.format(
                        "%02d",
                        picker.minute
                    ) + "AM"
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
            if (picker.hour == 12 && picker.minute>0) {

                binding.showTime2.text =
                    String.format("%02d", picker.hour) + " : " + String.format(
                        "%02d",
                        picker.minute
                    ) + "PM"
                second_time = String.format("%02d", picker.hour) + " : " + String.format(
                    "%02d",
                    picker.minute
                ) + "PM"
            } else  if (picker.hour > 12) {

                binding.showTime2.text =
                    String.format("%02d", picker.hour - 12) + " : " + String.format(
                        "%02d",
                        picker.minute
                    ) + "PM"
                second_time = String.format("%02d", picker.hour - 12) + " : " + String.format(
                    "%02d",
                    picker.minute
                ) + "PM"
            } else {
                binding.showTime2.text =
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

            flag1 = 10
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


    private fun intervalDialog(){
        val dialog = Dialog(this)
        dialog.setTitle("Select Day")
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.interval_dialog)
        dialog.context.setTheme(R.style.CustomDialog)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)

        val minusBt = dialog.findViewById<ImageView>(R.id.minusBt)
        val number_text = dialog.findViewById<TextView>(R.id.number)
        val plusBt = dialog.findViewById<ImageView>(R.id.plusBt)
        val cancel = dialog.findViewById<Button>(R.id.cancel)
        val save = dialog.findViewById<Button>(R.id.save)
        var counter = 1

        plusBt.setOnClickListener {
            if (counter<10){
                counter ++
            }
            intervalnumber = counter
            number_text.text = intervalnumber.toString()
        }

        minusBt.setOnClickListener {
            if (counter>1){
                counter--
            }
            intervalnumber = counter / 1
            number_text.text = intervalnumber.toString()
        }

        save.setOnClickListener {
           binding.intervalDays.text = "Alarm will repeat after $intervalnumber days"
            dialog.dismiss()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

        private fun customDialog(){
            val dialog = Dialog(this)
            dialog.setTitle("Select Day")
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.custom_dialog)
            dialog.context.setTheme(R.style.CustomDialog)
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)

            val oneTimes = dialog.findViewById<TextView>(R.id.oneTv)
            val twoTimes = dialog.findViewById<TextView>(R.id.twoTv)
            val threeTimes = dialog.findViewById<TextView>(R.id.threeTv)

            oneTimes.setOnClickListener {
                binding.showTime2.isVisible = false
                binding.showTime3.isVisible = false
                dialog.dismiss()
            }
            twoTimes.setOnClickListener {
                binding.showTime2.isVisible = true
                binding.showTime3.isVisible = false
                dialog.dismiss()
            }
            threeTimes.setOnClickListener {
                binding.showTime2.isVisible = true
                binding.showTime3.isVisible = true
                dialog.dismiss()
            }
            dialog.show()
        }

        private fun openDialog() {

            val sharedPreferences = getSharedPreferences("my_sharedPreference", 0)
            val editor = sharedPreferences.edit()
            val dialog = Dialog(this)
            dialog.setTitle("Select Day")
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.item_dialog)
            dialog.context.setTheme(R.style.CustomDialog)
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)

            val dialogSat = dialog.findViewById<CheckBox>(R.id.saturday)
            val dialogSun = dialog.findViewById<CheckBox>(R.id.sunday)
            val dialogMon = dialog.findViewById<CheckBox>(R.id.monday)
            val dialogTue = dialog.findViewById<CheckBox>(R.id.tuesday)
            val dialogWed = dialog.findViewById<CheckBox>(R.id.wednesday)
            val dialogThu = dialog.findViewById<CheckBox>(R.id.thursday)
            val dialogFri = dialog.findViewById<CheckBox>(R.id.friday)
            val saveBtn = dialog.findViewById<Button>(R.id.saveBn)
            val cancelBtn = dialog.findViewById<Button>(R.id.cancelBn)
            dialog.show()

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            if (flag > 0) {
                val satu = sharedPreferences.getBoolean("sat", true)
                dialogSat.isChecked = satu
                val sund = sharedPreferences.getBoolean("sun", true)
                dialogSun.isChecked = sund
                val mond = sharedPreferences.getBoolean("mon", true)
                dialogMon.isChecked = mond
                val tues = sharedPreferences.getBoolean("tue", true)
                dialogTue.isChecked = tues
                val wedn = sharedPreferences.getBoolean("wed", true)
                dialogWed.isChecked = wedn
                val thus = sharedPreferences.getBoolean("thu", true)
                dialogThu.isChecked = thus
                val frid = sharedPreferences.getBoolean("fri", true)
                dialogFri.isChecked = frid
            }

            saveBtn.setOnClickListener {

                if (dialogSat.isChecked) {
                    sat = "1"
                    editor.putBoolean("sat", true)
                    editor.apply()
                    stringBuffer.append("Saturday ")
                } else {
                    editor.putBoolean("sat", false)
                    editor.apply()
                }
                if (dialogSun.isChecked) {
                    sun = "2"
                    editor.putBoolean("sun", true)
                    editor.apply()
                    stringBuffer.append("Sunday ")
                } else {
                    editor.putBoolean("sun", false)
                    editor.apply()
                }
                if (dialogMon.isChecked) {
                    mon = "3"
                    editor.putBoolean("mon", true)
                    editor.apply()
                    stringBuffer.append("Monday ")
                } else {
                    editor.putBoolean("mon", false)
                    editor.apply()
                }
                if (dialogTue.isChecked) {
                    tue = "4"
                    editor.putBoolean("tue", true)
                    editor.apply()
                    stringBuffer.append("Tuesday ")
                } else {
                    editor.putBoolean("tue", false)
                    editor.apply()
                }
                if (dialogWed.isChecked) {
                    wed = "5"
                    editor.putBoolean("wed", true)
                    editor.apply()
                    stringBuffer.append("Wednesday ")
                } else {
                    editor.putBoolean("wed", false)
                    editor.apply()
                }
                if (dialogThu.isChecked) {
                    thu = "6"
                    editor.putBoolean("thu", true)
                    editor.apply()
                    stringBuffer.append("Thursday ")
                } else {
                    editor.putBoolean("thu", false)
                    editor.apply()
                }

                if (dialogFri.isChecked) {
                    fri = "7"
                    editor.putBoolean("fri", true)
                    editor.apply()
                    stringBuffer.append("Friday ")
                } else {
                    editor.putBoolean("fri", false)
                    editor.apply()

                }
                flag++
                binding.weekTv2.text = stringBuffer.toString()
                stringBuffer.setLength(0)
                dialog.dismiss()
            }
        }
    }
