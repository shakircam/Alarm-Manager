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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.timepicker.MaterialTimePicker
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.data.adapter.AlarmAdapter
import com.itmedicus.randomuser.data.adapter.ItemClickListener
import com.itmedicus.randomuser.data.local.UserDatabase
import com.itmedicus.randomuser.databinding.ActivityAlarmBinding
import com.itmedicus.randomuser.databinding.ActivityShowAlarmBinding
import com.itmedicus.randomuser.model.AlarmTime
import com.itmedicus.randomuser.model.AlarmTimeAndMultipleAlarm
import com.itmedicus.randomuser.model.MultipleAlarm
import com.itmedicus.randomuser.model.RequestCode
import com.itmedicus.randomuser.utils.ClickListener
import com.itmedicus.randomuser.utils.SwipeToDelete
import kotlinx.coroutines.launch
import java.util.*

class ShowAlarmActivity : AppCompatActivity(),ClickListener {
    private lateinit var binding: ActivityShowAlarmBinding
    lateinit var context: Context
    private lateinit var alarmManager: AlarmManager

    private val adapter by lazy { AlarmAdapter(context,this) }
    var list = mutableListOf<AlarmTime>()
    private var multipleAlarm = mutableListOf<RequestCode>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

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

    override fun onItemCancel(position: Int) {

        val item = list[position]
        lifecycleScope.launch {
           val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
           val multipleAlarmList = db.getAlarmRequestCode(item.time)
           multipleAlarm.addAll(multipleAlarmList)
       }

        val intent = Intent(this, AlarmCreateActivity.AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,item.requestCode,intent,0)
        alarmManager.cancel(pendingIntent)

        Toast.makeText(this,"alarm cancel!!",Toast.LENGTH_SHORT).show()
        Log.d("this", "cancel alarm..")
        Log.d("this", "${item.requestCode}")

   /*     if (multipleAlarm.size > 1){

                multipleAlarm[0].requestCode
                multipleAlarm[1].requestCode
                val pending = PendingIntent.getBroadcast(this@ShowAlarmActivity,multipleAlarm[0].requestCode,intent,0)
                alarmManager.cancel(pending)

                Log.d("this", "cancel multiple"+multipleAlarm[0].requestCode.toString())
                Log.d("this", "cancel multiple"+multipleAlarm[1].requestCode.toString())
                val pen = PendingIntent.getBroadcast(this@ShowAlarmActivity,multipleAlarm[1].requestCode,intent,0)
                alarmManager.cancel(pen)
                Toast.makeText(this,"alarm cancel!!",Toast.LENGTH_SHORT).show()
        }else{

            val intent = Intent(this, AlarmCreateActivity.AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this,item.requestCode,intent,0)
            alarmManager.cancel(pendingIntent)

            Toast.makeText(this,"alarm cancel!!",Toast.LENGTH_SHORT).show()
            Log.d("this", "cancel alarm..")
            Log.d("this", "${item.requestCode}")
        }*/

    }

    override fun onAlarm(position: Int) {
        val item = list[position]
        val time = item.calenderTime
        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
            val multipleAlarmList = db.getAlarmRequestCode(item.time)
            multipleAlarm.addAll(multipleAlarmList)
        }

        val intent = Intent(this,AlarmCreateActivity.AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,item.requestCode,intent,0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            time,
            24*60*60*1000 ,
            pendingIntent)
        Log.d("this", "on alarm again.."+item.requestCode)
        Toast.makeText(this,"alarm on again!!",Toast.LENGTH_SHORT).show()

      /*  if (multipleAlarm.size > 1){
            multipleAlarm[0].requestCode
            multipleAlarm[1].requestCode

            val pending = PendingIntent.getBroadcast(this,multipleAlarm[0].requestCode,intent,0)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                24*60*60*1000 * 7,
                pending)
            //
            val pen = PendingIntent.getBroadcast(this,multipleAlarm[1].requestCode,intent,0)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                24*60*60*1000 * 7,
                pen)
            Log.d("this", "on alarm again.."+multipleAlarm[0].requestCode)
            Toast.makeText(this,"alarm on again!!",Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(this,AlarmCreateActivity.AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this,item.requestCode,intent,0)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                24*60*60*1000 ,
                pendingIntent)
            Log.d("this", "on alarm again.."+item.requestCode)
            Toast.makeText(this,"alarm on again!!",Toast.LENGTH_SHORT).show()
        }*/

    }


    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        swipeToDelete(mRecyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.list[viewHolder.adapterPosition]
                // Delete Item
                lifecycleScope.launch {
                    val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
                    db.deleteAlarm(deletedItem)

                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                    Toast.makeText(this@ShowAlarmActivity,"alarm deleted",Toast.LENGTH_SHORT).show()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    class AlarmReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {

            Log.d("this", "Receive alarm: " + Date().toString())
            // Set the alarm here.
            val vibrator = context?.getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        5000,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
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