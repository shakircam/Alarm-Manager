package com.itmedicus.randomuser.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_table")
data class AlarmTime(
    val time: String,
    val title : String
)  {
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}
