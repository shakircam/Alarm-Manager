package com.itmedicus.randomuser.data.repository

import androidx.lifecycle.LiveData
import com.itmedicus.randomuser.data.local.UserDao
import com.itmedicus.randomuser.model.AlarmTime
import com.itmedicus.randomuser.model.BmiRecord

class AlarmRepository(private val alarmDao: UserDao) {

    val getAllAlarm: LiveData<MutableList<AlarmTime>> = alarmDao.getAllAlarm()

    val getAllBmiResult: LiveData<MutableList<BmiRecord>> = alarmDao.getAllBmiResult()


    suspend fun  insertAlarmTime(alarmTime: AlarmTime){
        alarmDao.insertAlarmTime(alarmTime)
    }

    suspend fun insertBmiData(bmiRecord: BmiRecord){
        alarmDao.insertBmiData(bmiRecord)
    }


    suspend fun deleteBmiData(bmiRecord: BmiRecord){
        alarmDao.deleteBmiData(bmiRecord)
    }


    suspend fun deleteItem(alarmTime: AlarmTime){
        alarmDao.deleteAlarm(alarmTime)
    }

    suspend fun deleteMultipleAlarm(id: Long){
        alarmDao.deleteMultipleAlarm(id)
    }

    suspend fun deleteTwoTimesAlarm(id: Long){
        alarmDao.deleteTwoTimesAlarm(id)
    }
}