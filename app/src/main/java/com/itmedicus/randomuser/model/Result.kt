package com.itmedicus.randomuser.model


import androidx.room.*


@Entity(tableName = "user_table",indices = [Index(value = ["name"], unique = true)])
data class Result(

    val name: String,
    val phone : String,
    val gender : String,
    val email : String,
    val nat : String,
    val picture: String,
    val location: String,
){
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}


