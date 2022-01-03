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
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.itmedicus.randomuser.AlarmReceiver
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
import com.itmedicus.randomuser.ui.viewmodel.AlarmViewModel
import com.itmedicus.randomuser.utils.ClickListener
import com.itmedicus.randomuser.utils.SwipeToDelete
import kotlinx.coroutines.launch
import java.util.*

class ShowAlarmActivity : AppCompatActivity(),ClickListener {
    private lateinit var binding: ActivityShowAlarmBinding
    lateinit var context: Context
    private lateinit var alarmManager: AlarmManager
    private lateinit var myViewModel: AlarmViewModel
    private val adapter by lazy { AlarmAdapter(this) }
    var list = mutableListOf<AlarmTime>()
    private var multipleAlarm = mutableListOf<RequestCode>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        myViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        initRecyclerView()
        binding.fab.setOnClickListener {
            val intent = Intent(this,AlarmCreateActivity::class.java)
            startActivity(intent)
        }

        myViewModel.getAllData.observe(this,{
            list.addAll(it)
            adapter.setData(it)
        })

    }

    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

    }

    override fun onItemCancel(position: Int) {

        val item = list[position]
        val id = item.id

        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
            val multipleAlarmList = db.getAlarmRequestCode(id)
            db.updateSwitchButtonState(false,id)
            multipleAlarm.addAll(multipleAlarmList)
        }

        if (multipleAlarm.size > 1){

            multipleAlarm[0].requestCode
            multipleAlarm[1].requestCode
            intent.action = "okay"
            val intent = Intent(this, AlarmReceiver::class.java)
            val pending = PendingIntent.getBroadcast(this,multipleAlarm[0].requestCode,intent,0)
            alarmManager.cancel(pending)
            Log.d("this", "cancel multiple"+multipleAlarm[0].requestCode.toString())

            val pen = PendingIntent.getBroadcast(this,multipleAlarm[1].requestCode,intent,0)
            alarmManager.cancel(pen)
            Log.d("this", "cancel multiple"+multipleAlarm[1].requestCode.toString())
            Toast.makeText(this,"alarm cancel!!",Toast.LENGTH_SHORT).show()

            if (multipleAlarm.size > 2)
            {   intent.action = "okay"
                val reqThree = multipleAlarm[2].requestCode
                val pend = PendingIntent.getBroadcast(this,reqThree,intent,0)
                alarmManager.cancel(pend)
                Log.d("this", "cancel multiple"+multipleAlarm[2].requestCode.toString())
            }
            if (multipleAlarm.size > 3)
            {   intent.action = "okay"
                val reqThree = multipleAlarm[3].requestCode
                val pend = PendingIntent.getBroadcast(this,reqThree,intent,0)
                alarmManager.cancel(pend)
                Log.d("this", "cancel multiple"+multipleAlarm[3].requestCode.toString())
            }
            if (multipleAlarm.size > 4)
            {   intent.action = "okay"
                val reqThree = multipleAlarm[4].requestCode
                val pend = PendingIntent.getBroadcast(this,reqThree,intent,0)
                alarmManager.cancel(pend)
                Log.d("this", "cancel multiple"+multipleAlarm[4].requestCode.toString())
            }
            if (multipleAlarm.size > 5)
            {   intent.action = "okay"
                val reqThree = multipleAlarm[5].requestCode
                val pend = PendingIntent.getBroadcast(this,reqThree,intent,0)
                alarmManager.cancel(pend)
                Log.d("this", "cancel multiple"+multipleAlarm[5].requestCode.toString())
            }

        }else{

            val intent = Intent(this, AlarmReceiver::class.java)
            intent.action = "okay"
            val pendingIntent = PendingIntent.getBroadcast(this,item.requestCode,intent,0)

            alarmManager.cancel(pendingIntent)

            Toast.makeText(this,"alarm off!!",Toast.LENGTH_SHORT).show()
            Log.d("this", "cancel alarm..")
            Log.d("this", "alarm id::$id")
            Log.d("this", "request code:::${item.requestCode}")
        }
    }



    override fun onAlarm(position: Int) {

        val item = list[position]
        val time = item.calenderTime
        var dbTime = time
        val id = item.id
        val currentTime = System.currentTimeMillis()
        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
            val multipleAlarmList = db.getAlarmRequestCode(id)
            db.updateSwitchButtonState(true, id)
            multipleAlarm.addAll(multipleAlarmList)
        }
        val intent = Intent(this,AlarmReceiver::class.java)
        intent.action = "okay"
        intent.putExtra("time", time)
        if (multipleAlarm.size > 1){
            multipleAlarm[0].requestCode
            multipleAlarm[1].requestCode


            val pending = PendingIntent.getBroadcast(this,multipleAlarm[0].requestCode,intent,0)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                24*60*60*1000 * 7,
                pending)
            Log.d("this", "on alarm again.."+multipleAlarm[0].requestCode)


            val pen = PendingIntent.getBroadcast(this,multipleAlarm[1].requestCode,intent,0)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                24*60*60*1000 * 7,
                pen)
            Log.d("this", "on alarm again.."+multipleAlarm[1].requestCode)
            Toast.makeText(this,"alarm on again!!",Toast.LENGTH_SHORT).show()

            if(multipleAlarm.size > 2){

                val pend = PendingIntent.getBroadcast(this,multipleAlarm[2].requestCode,intent,0)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    24*60*60*1000 * 7,
                    pend)
                Log.d("this", "on alarm again..$multipleAlarm[2].requestCode")
                Toast.makeText(this,"alarm on again!!",Toast.LENGTH_SHORT).show()
            }
            if(multipleAlarm.size > 3){

                val pend = PendingIntent.getBroadcast(this,multipleAlarm[3].requestCode,intent,0)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    24*60*60*1000 * 7,
                    pend)
                Log.d("this", "on alarm again..$multipleAlarm[3].requestCode")
                Toast.makeText(this,"alarm on again!!",Toast.LENGTH_SHORT).show()
            }
            if(multipleAlarm.size > 4){

                val pend = PendingIntent.getBroadcast(this,multipleAlarm[4].requestCode,intent,0)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    24*60*60*1000 * 7,
                    pend)
                Log.d("this", "on alarm again..$multipleAlarm[4].requestCode")
                Toast.makeText(this,"alarm on again!!",Toast.LENGTH_SHORT).show()
            }
            if(multipleAlarm.size > 5){

                val pend = PendingIntent.getBroadcast(this,multipleAlarm[5].requestCode,intent,0)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    24*60*60*1000 * 7,
                    pend)
                Log.d("this", "on alarm again..$multipleAlarm[5].requestCode")
                Toast.makeText(this,"alarm on again!!",Toast.LENGTH_SHORT).show()
            }
            if(multipleAlarm.size > 6){

                val pend = PendingIntent.getBroadcast(this,multipleAlarm[6].requestCode,intent,0)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    24*60*60*1000 * 7,
                    pend)
                Log.d("this", "on alarm again..$multipleAlarm[6].requestCode")
                Toast.makeText(this,"alarm on again!!",Toast.LENGTH_SHORT).show()
            }
        }else{
            Log.d("this", "db time before increment::$time")
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = "okay"
            intent.putExtra("time", time)
            val pendingIntent = PendingIntent.getBroadcast(context,item.requestCode,intent,0)
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    dbTime,
                    24*60*60*1000 ,
                    pendingIntent)

            Log.d("this", "Alarm on again.."+item.requestCode)
            Log.d("this", "alarm id::$id")
            Log.d("this", "current time..$currentTime")
            Log.d("this", "alarm time increment::$dbTime")
            Toast.makeText(this,"alarm on again!!",Toast.LENGTH_SHORT).show()
        }
    }

    override fun deleteAlarm(alarmTime: AlarmTime,position: Int) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Alarm?")
        builder.setIcon(R.drawable.ic_cancel)
        builder.setMessage("Do you want to delete the alarm?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { _, _ ->
            myViewModel.deleteAlarm(alarmTime)
           // cancelAlarm(position)
            Toast.makeText(this,"successfully delete the alarm",Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            Toast.makeText(this,"Alarm not deleted",Toast.LENGTH_SHORT).show()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()

    }

   fun cancelAlarm(position : Int){

   }

}