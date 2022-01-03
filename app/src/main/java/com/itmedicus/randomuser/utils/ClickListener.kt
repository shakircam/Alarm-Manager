package com.itmedicus.randomuser.utils

import com.itmedicus.randomuser.model.AlarmTime

interface ClickListener {
    fun onItemCancel(position : Int)
    fun onAlarm(position: Int)
    fun deleteAlarm(alarmTime: AlarmTime,position: Int)
}