package com.itmedicus.randomuser.ui.activity

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.itmedicus.randomuser.utils.SwipeToDelete
import kotlinx.coroutines.launch
import java.util.*

class ShowAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowAlarmBinding
    lateinit var context: Context
    lateinit var alarmManager: AlarmManager

    private val adapter by lazy { AlarmAdapter(context) }
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

    class Receiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("this","Receive alarm: "+Date().toString())
            val intent = Intent(context,ShowAlarmActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent=  PendingIntent.getActivity(context,0,intent,0)

            val builder = NotificationCompat.Builder(context!!,"1000")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Alarm Clock")
                .setContentText("Alarm manager running")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(111,builder.build())

        }

    }

}