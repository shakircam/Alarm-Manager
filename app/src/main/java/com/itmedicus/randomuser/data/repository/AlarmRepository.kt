package com.itmedicus.randomuser.data.repository

import androidx.lifecycle.LiveData
import com.itmedicus.randomuser.data.local.UserDao
import com.itmedicus.randomuser.model.AlarmTime

class AlarmRepository(private val alarmDao: UserDao) {

    val getAllAlarm: LiveData<MutableList<AlarmTime>> = alarmDao.getAllAlarm()

    suspend fun deleteItem(alarmTime: AlarmTime){
        alarmDao.deleteAlarm(alarmTime)
    }

    suspend fun  insertAlarmTime(alarmTime: AlarmTime){
        alarmDao.insertAlarmTime(alarmTime)
    }

    suspend fun deleteMultipleAlarm(id: Long){
        alarmDao.deleteMultipleAlarm(id)
    }
}