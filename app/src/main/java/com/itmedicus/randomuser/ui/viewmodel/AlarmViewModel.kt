package com.itmedicus.randomuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.itmedicus.randomuser.data.local.UserDatabase
import com.itmedicus.randomuser.data.repository.AlarmRepository
import com.itmedicus.randomuser.model.AlarmTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val alarmDao = UserDatabase.getDatabase(
        application
    ).userDao()

    private val repository: AlarmRepository = AlarmRepository(alarmDao)
    val getAllData: LiveData<MutableList<AlarmTime>> = repository.getAllAlarm

    fun deleteAlarm(alarmTime: AlarmTime) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(alarmTime)
        }
    }

    fun insertAlarm(alarmTime: AlarmTime){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlarmTime(alarmTime)
        }
    }

    fun deleteMultipleAlarm(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMultipleAlarm(id)
        }
    }
}