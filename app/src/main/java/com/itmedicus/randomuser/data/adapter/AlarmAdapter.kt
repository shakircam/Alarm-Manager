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
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivities
import androidx.recyclerview.widget.RecyclerView
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.model.AlarmTime
import com.itmedicus.randomuser.ui.activity.AlarmCreateActivity
import com.itmedicus.randomuser.utils.ClickListener
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import android.widget.*
import com.google.android.material.chip.Chip


class AlarmAdapter(private val clickListener: ClickListener): RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> () {
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
        holder.type.text = currentItem.amount.toString()+" "+currentItem.type
        holder.switchButton.isChecked = currentItem.flag

        holder.deleteBt.setOnClickListener {
            clickListener.deleteAlarm(currentItem,position)
        }

         holder.switchButton.setOnClickListener {

              if(holder.switchButton.isChecked){
                  clickListener.onAlarm(position)
              }else{
                  clickListener.onItemCancel(position)
              }
          }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class AlarmViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val time = itemView.findViewById(R.id.showTime) as TextView
        val title = itemView.findViewById(R.id.titleTv) as TextView
        val status = itemView.findViewById(R.id.statusTv) as Chip
        val switchButton = itemView.findViewById(R.id.startBn) as SwitchCompat
        val deleteBt = itemView.findViewById(R.id.editBt) as ImageView
        val type = itemView.findViewById(R.id.type) as TextView
    }

    fun setData(userList: MutableList<AlarmTime>){
        this.list = userList
        notifyDataSetChanged()
    }
}