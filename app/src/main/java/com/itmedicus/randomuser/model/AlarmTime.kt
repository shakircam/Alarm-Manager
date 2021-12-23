package com.itmedicus.randomuser.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "alarm_table")
data class AlarmTime(
    val time: String,
    val calenderTime : Long,
    val title : String,
    val requestCode : Int,
    val status : String
)  {
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}

@Entity(tableName = "multiple_alarm_table")
 data class MultipleAlarm(

    val time: String,
    val calenderTime : Long,
    val title : String,
    val day : String,
    val requestCode : Int,
 ) {
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}

 data class AlarmTimeAndMultipleAlarm(
     @Embedded val alarmTime: AlarmTime,
     @Relation (
         parentColumn = "id",
         entityColumn = "day"
             )
     val alarmLists: List<MultipleAlarm>
 )
