package com.itmedicus.randomuser.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE



@Entity(tableName = "alarm_table")
data class AlarmTime(
    val time: String,
    val type : String,
    val amount : Int,
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val calenderTime : Long,
    val title : String,
    val requestCode : Int,
    val status : String,
    val flag :  Boolean
)

@Entity(tableName = "multiple_alarm_table")
    /*foreignKeys = [
        ForeignKey(
            entity = AlarmTime::class,
            parentColumns = ["calenderTime"],
            childColumns = ["fk_id"],
            onDelete = CASCADE
        )])*/
 data class MultipleAlarm(

    val time: String,
    val calenderTime : Long,
    val fk_id : Long,
    val day : String,
    val requestCode : Int,
 ) {
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}

@Entity(tableName = "two_times_table")
data class TwoTimesAlarm(

    val time: String,
    val calenderTime : Long,
    val fk_id : Long,
    val day : String,
    val requestCode : Int,
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}


 data class AlarmTimeAndMultipleAlarm(
     @Embedded val alarmTime: AlarmTime,
     @Relation (
         parentColumn = "calenderTime",
         entityColumn = "fk_id"
             )
     val alarmLists: List<MultipleAlarm>
 )
