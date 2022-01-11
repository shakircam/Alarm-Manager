package com.itmedicus.randomuser.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itmedicus.randomuser.AlarmReceiver
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.data.adapter.AlarmAdapter
import com.itmedicus.randomuser.data.local.UserDatabase
import com.itmedicus.randomuser.databinding.ActivityShowAlarmBinding
import com.itmedicus.randomuser.model.AlarmTime
import com.itmedicus.randomuser.model.RequestCode
import com.itmedicus.randomuser.ui.viewmodel.AlarmViewModel
import com.itmedicus.randomuser.utils.ClickListener
import kotlinx.coroutines.*
import java.util.*

class ShowAlarmActivity : AppCompatActivity(),ClickListener {
    private lateinit var binding: ActivityShowAlarmBinding
    lateinit var context: Context
    private lateinit var alarmManager: AlarmManager
    private lateinit var myViewModel: AlarmViewModel
    private val adapter by lazy { AlarmAdapter(this) }
    var list = mutableListOf<AlarmTime>()
    private var multipleAlarm = mutableListOf<RequestCode>()
    private var dbTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        context = this
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        myViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        initRecyclerView()
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.fab.setOnClickListener {
            val intent = Intent(this,AlarmCreateActivity::class.java)
            startActivity(intent)
        }

        myViewModel.getAllData.observe(this,{
            list.addAll(it)
            adapter.setData(it)
            if (it.isEmpty()){
                binding.cons.isVisible = true
            }
        })

    }

    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // if the recycler view is scrolled
                // above shrink the FAB
                if (dy > 5 && binding.fab.isExtended) {
                    binding.fab.shrink()
                }

                // if the recycler view is scrolled
                // above extend the FAB
                if (dy < -5 && !binding.fab.isExtended) {
                    binding.fab.extend()
                }

                // of the recycler view is at the first
                // item always extend the FAB
                if (!recyclerView.canScrollVertically(-1)) {
                    binding.fab.extend()
                }
            }
        })

    }
    override fun onItemCancel(position: Int) {
        cancelAlarm(position)
    }

    @DelicateCoroutinesApi
    override fun onAlarm(position: Int) {

        val item = list[position]
        var time = item.calenderTime
        dbTime = time
        val id = item.id
        val currentTime = System.currentTimeMillis()

        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
            val multipleAlarmList = db.getAlarmRequestCode(id)
            db.updateSwitchButtonState(true, id)
            multipleAlarm.addAll(multipleAlarmList)
        }

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "okay"
        intent.putExtra("time", time)

        if (multipleAlarm.size >= 1) {

            multipleAlarm[0].requestCode
            var listOneTime = multipleAlarm[0].calenderTime

            GlobalScope.launch(Dispatchers.IO) {
                while (listOneTime<currentTime){
                    listOneTime += 86400000*7
                    Log.d("this", " time is incrementing[1]::$listOneTime")
                }
                val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
                db.updateAlarmTimeInMultipleDay(listOneTime,multipleAlarm[0].id)
                db.updateAlarmTime(listOneTime,id)
            }

            val pending = PendingIntent.getBroadcast(this, multipleAlarm[0].requestCode, intent, 0)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                listOneTime,
                24 * 60 * 60 * 1000 * 7,
                pending
            )
            Log.d("this", "on alarm again[1].." + multipleAlarm[0].requestCode)
            Log.d("this", " time after increment[1]::$listOneTime")


            multipleAlarm[1].requestCode
            var listTwoTime = multipleAlarm[1].calenderTime
            GlobalScope.launch(Dispatchers.IO) {
                while (listTwoTime<currentTime){
                    listTwoTime += 86400000*7
                    Log.d("this", " time is incrementing[2]::$listTwoTime")
                }

                val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
                db.updateAlarmTimeInMultipleDay(listTwoTime,multipleAlarm[1].id)
            }

            val pen = PendingIntent.getBroadcast(this, multipleAlarm[1].requestCode, intent, 0)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                listTwoTime,
                24 * 60 * 60 * 1000 * 7,
                pen
            )
            Log.d("this", "on alarm again.." + multipleAlarm[1].requestCode)
            Log.d("this", " time after increment[2]::$listTwoTime")
            Toast.makeText(this, "alarm on again!!", Toast.LENGTH_SHORT).show()

            if (multipleAlarm.size > 2) {

                var listThreeTime = multipleAlarm[2].calenderTime

                GlobalScope.launch(Dispatchers.IO) {
                    while (listThreeTime<currentTime){
                        listThreeTime += 86400000*7
                        Log.d("this", " time is incrementing[3]::$listThreeTime")
                    }

                    val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
                    db.updateAlarmTimeInMultipleDay(listThreeTime,multipleAlarm[2].id)
                }
                val pend = PendingIntent.getBroadcast(this, multipleAlarm[2].requestCode, intent, 0)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    listThreeTime,
                    24 * 60 * 60 * 1000 * 7,
                    pend
                )
                Log.d("this", "on alarm again..$multipleAlarm[2].requestCode")
                Log.d("this", " time after increment[3]::$listThreeTime")
                Toast.makeText(this, "alarm on again!!", Toast.LENGTH_SHORT).show()
            }
            if (multipleAlarm.size > 3) {

                var listFourTime = multipleAlarm[3].calenderTime

                GlobalScope.launch(Dispatchers.IO) {
                    while (listFourTime<currentTime){
                        listFourTime += 86400000*7
                        Log.d("this", " time is incrementing[4]::$listFourTime")
                    }

                    val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
                    db.updateAlarmTimeInMultipleDay(listFourTime,multipleAlarm[3].id)
                }
                val pend = PendingIntent.getBroadcast(this, multipleAlarm[3].requestCode, intent, 0)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    listFourTime,
                    24 * 60 * 60 * 1000 * 7,
                    pend
                )
                Log.d("this", "on alarm again..$multipleAlarm[3].requestCode")
                Log.d("this", " time after increment[4]::$listFourTime")
                Toast.makeText(this, "alarm on again!!", Toast.LENGTH_SHORT).show()
            }
            if (multipleAlarm.size > 4) {

                var listFiveTime = multipleAlarm[4].calenderTime

                GlobalScope.launch(Dispatchers.IO) {
                    while (listFiveTime<currentTime){
                        listFiveTime += 86400000*7
                        Log.d("this", " time is incrementing[5]::$listFiveTime")
                    }

                    val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
                    db.updateAlarmTimeInMultipleDay(listFiveTime,multipleAlarm[4].id)
                }
                val pend = PendingIntent.getBroadcast(this, multipleAlarm[4].requestCode, intent, 0)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    listFiveTime,
                    24 * 60 * 60 * 1000 * 7,
                    pend
                )
                Log.d("this", "on alarm again..$multipleAlarm[4].requestCode")
                Log.d("this", " time after increment[5]::$listFiveTime")
                Toast.makeText(this, "alarm on again!!", Toast.LENGTH_SHORT).show()
            }
            if (multipleAlarm.size > 5) {

                var listSixTime = multipleAlarm[5].calenderTime

                GlobalScope.launch(Dispatchers.IO) {
                    while (listSixTime<currentTime){
                        listSixTime += 86400000*7
                        Log.d("this", " time is incrementing::$listSixTime")
                    }

                    val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
                    db.updateAlarmTimeInMultipleDay(listSixTime,multipleAlarm[5].id)
                }
                val pend = PendingIntent.getBroadcast(this, multipleAlarm[5].requestCode, intent, 0)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    listSixTime,
                    24 * 60 * 60 * 1000 * 7,
                    pend
                )
                Log.d("this", "on alarm again..$multipleAlarm[5].requestCode")
                Log.d("this", " time after increment::$listSixTime")
                Toast.makeText(this, "alarm on again!!", Toast.LENGTH_SHORT).show()
            }
            if (multipleAlarm.size > 6) {

                var listSevenTime = multipleAlarm[6].calenderTime
                GlobalScope.launch(Dispatchers.IO) {
                    while (listSevenTime<currentTime){
                        listSevenTime += 86400000*7
                        Log.d("this", " time is incrementing::$listSevenTime")
                    }

                    val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
                    db.updateAlarmTimeInMultipleDay(listSevenTime,multipleAlarm[6].id)
                }
                val pend = PendingIntent.getBroadcast(this, multipleAlarm[6].requestCode, intent, 0)
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    listSevenTime,
                    24 * 60 * 60 * 1000 * 7,
                    pend
                )
                Log.d("this", "on alarm again..$multipleAlarm[6].requestCode")
                Log.d("this", " time after increment[7]::$listSevenTime")
                Toast.makeText(this, "alarm on again!!", Toast.LENGTH_SHORT).show()
            }
        } else {

            Log.d("this", "db time before increment::$time")
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = "okay"
            intent.putExtra("time", time)
            val pendingIntent = PendingIntent.getBroadcast(context, item.requestCode, intent, 0)

            GlobalScope.launch(Dispatchers.IO) {

                    while (time<currentTime){
                        time += 86400000
                        Log.d("this", " time is incrementing::$time")
                    }

                    val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
                    db.updateAlarmTime(time,id)

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    24*60*60*1000,
                    pendingIntent
                )

                Log.d("this", " current time..$currentTime")
                Log.d("this", " time after increment::$time")
            }

            Log.d("this", "Alarm on again::request code.." + item.requestCode)
            Toast.makeText(this, "alarm on again!!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun deleteAlarm(alarmTime: AlarmTime,position: Int) {

        val item = list[position]
        val id = item.id

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Alarm?")
        builder.setIcon(R.drawable.ic_cancel)
        builder.setMessage("Do you want to delete the alarm?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { _, _ ->
            myViewModel.deleteAlarm(alarmTime)
            cancelAlarm(position)
            if (multipleAlarm.size >=1){
                myViewModel.deleteMultipleAlarm(id)
            }
            Toast.makeText(this,"successfully delete the alarm",Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            Toast.makeText(this,"Alarm not deleted",Toast.LENGTH_SHORT).show()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()

    }

   private fun cancelAlarm(position : Int){
       val item = list[position]
       val id = item.id

       lifecycleScope.launch {
           val db = UserDatabase.getDatabase(this@ShowAlarmActivity).userDao()
           val multipleAlarmList = db.getAlarmRequestCode(id)
           db.updateSwitchButtonState(false,id)
           multipleAlarm.addAll(multipleAlarmList)
       }

       if (multipleAlarm.size >= 1){

           multipleAlarm[0].requestCode
           multipleAlarm[1].requestCode

           val intent = Intent(this, AlarmReceiver::class.java)
           intent.action = "okay"

           val pending = PendingIntent.getBroadcast(this,multipleAlarm[0].requestCode,intent,0)
           alarmManager.cancel(pending)
           Log.d("this", "cancel multiple"+multipleAlarm[0].requestCode.toString())

           val pen = PendingIntent.getBroadcast(this,multipleAlarm[1].requestCode,intent,0)
           alarmManager.cancel(pen)
           Log.d("this", "cancel multiple"+multipleAlarm[1].requestCode.toString())
           Toast.makeText(this,"alarm cancel!!",Toast.LENGTH_SHORT).show()

           if (multipleAlarm.size > 2) {

             //  intent.action = "okay"
               val reqThree = multipleAlarm[2].requestCode
               val pend = PendingIntent.getBroadcast(this,reqThree,intent,0)
               alarmManager.cancel(pend)
               Log.d("this", "cancel multiple"+multipleAlarm[2].requestCode.toString())
           }
           if (multipleAlarm.size > 3) {

              // intent.action = "okay"
               val reqThree = multipleAlarm[3].requestCode
               val pend = PendingIntent.getBroadcast(this,reqThree,intent,0)
               alarmManager.cancel(pend)
               Log.d("this", "cancel multiple"+multipleAlarm[3].requestCode.toString())
           }
           if (multipleAlarm.size > 4) {

               //intent.action = "okay"
               val reqThree = multipleAlarm[4].requestCode
               val pend = PendingIntent.getBroadcast(this,reqThree,intent,0)
               alarmManager.cancel(pend)
               Log.d("this", "cancel multiple"+multipleAlarm[4].requestCode.toString())
           }
           if (multipleAlarm.size > 5) {

               //intent.action = "okay"
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

}