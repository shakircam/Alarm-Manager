package com.itmedicus.randomuser.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bmi_table")
data class BmiRecord(
    val bmiScore : Double? = null,
    val bmiStatus : String? = null,
    val height : String? = null,
    val weight : Double? = null,
    val date : String? = null
){
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}
