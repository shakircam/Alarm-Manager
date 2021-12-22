package com.itmedicus.randomuser.data.adapter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivities
import androidx.recyclerview.widget.RecyclerView
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.model.AlarmTime
import com.itmedicus.randomuser.ui.activity.AlarmCreateActivity
import com.itmedicus.randomuser.utils.ClickListener

class AlarmAdapter(private val context: Context,private val clickListener: ClickListener): RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> () {
    var list = mutableListOf<AlarmTime>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val currentItem = list[position]

        holder.title.text = currentItem.title
        holder.time.text = currentItem.time
        holder.status.text = currentItem.status
        holder.editBt.setOnClickListener {
            clickListener.onAlarm(position)

        }
          holder.button.setOnClickListener {
              clickListener.onItemCancel(position)
          }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class AlarmViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val time = itemView.findViewById(R.id.showTime) as TextView
        val title = itemView.findViewById(R.id.titleTv) as TextView
        val status = itemView.findViewById(R.id.statusTv) as TextView
        val button = itemView.findViewById(R.id.startBn) as SwitchCompat
        val editBt = itemView.findViewById(R.id.editBt) as ImageView
    }

    fun setData(userList: MutableList<AlarmTime>){
        this.list = userList
        notifyDataSetChanged()
    }
}