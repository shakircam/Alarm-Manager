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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.itmedicus.randomuser.data.adapter.AlarmAdapter
import com.itmedicus.randomuser.data.adapter.HistoryAdapter
import com.itmedicus.randomuser.data.adapter.ItemClickListener
import com.itmedicus.randomuser.data.local.UserDatabase
import com.itmedicus.randomuser.model.AlarmTime
import com.itmedicus.randomuser.model.Result
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList


class ShowAlarmActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var binding: ActivityShowAlarmBinding
    lateinit var context: Context
    lateinit var alarmManager: AlarmManager
    private val CHANNEL_ID = "1000"
    private var request_code= 0
    private lateinit var picker : MaterialTimePicker
    private lateinit var calender : Calendar

    private val adapter by lazy { AlarmAdapter(context,this) }
    var list = mutableListOf<AlarmTime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        initRecyclerView()

        binding.fab.setOnClickListener {
            val intent = Intent(this,AlarmCreateActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
            val timeList = db.getAllAlarmTime()
            list.addAll(timeList)
            adapter.setData(list)
        }

    }

    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }


    override fun onItemSend(position: Int) {

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