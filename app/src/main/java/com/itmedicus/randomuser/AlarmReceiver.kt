package com.itmedicus.randomuser

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.itmedicus.randomuser.ui.activity.NotificationShowActivity
import com.itmedicus.randomuser.ui.activity.ShowAlarmActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

            val title = intent?.getStringExtra("time")
           // val id = intent?.getIntExtra("notification_id",1)

            // Set the alarm here.
            val time = System.currentTimeMillis()
            Log.d("this", "Receive alarm:::" + Date().toString()+":::$title"+"::: current time::: $time" )
            val vibrator = context?.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        8000,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(8000)
            }
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val r = RingtoneManager.getRingtone(context, notification)
            r.play()


            val intent = Intent(context, NotificationShowActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = "Cancel"
                putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, 0)
                putExtra("time",title)
            }

            val timeIntent = Intent(context,NotificationShowActivity::class.java)
                timeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                timeIntent.putExtra("time",title)


            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val builder = NotificationCompat.Builder(context, "1000")
                .setSmallIcon(R.drawable.notification_icon1)
                .setColor(Color.BLUE)
                .setContentTitle(title)
                .setContentText("Time to take your medicine")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_cancel, "Cancel",
                    pendingIntent)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(111, builder.build())
            //notificationManager.cancel(id!!)

    }
}