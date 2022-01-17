package com.itmedicus.randomuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.itmedicus.randomuser.data.local.UserDatabase
import com.itmedicus.randomuser.data.repository.AlarmRepository
import com.itmedicus.randomuser.model.AlarmTime
import com.itmedicus.randomuser.model.BmiRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val alarmDao = UserDatabase.getDatabase(
        application
    ).userDao()

    private val repository: AlarmRepository = AlarmRepository(alarmDao)
    val getAllData: LiveData<MutableList<AlarmTime>> = repository.getAllAlarm
    val getAllBmiResult: LiveData<MutableList<BmiRecord>> = repository.getAllBmiResult



    fun insertAlarm(alarmTime: AlarmTime){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlarmTime(alarmTime)
        }
    }


    fun insertBmiData(bmiRecord: BmiRecord){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBmiData(bmiRecord)
        }
    }

    fun deleteBmiData(bmiRecord: BmiRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBmiData(bmiRecord)
        }
    }

    fun deleteAlarm(alarmTime: AlarmTime) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(alarmTime)
        }
    }

    fun deleteMultipleAlarm(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMultipleAlarm(id)
        }
    }
}