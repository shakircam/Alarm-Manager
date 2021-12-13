package com.itmedicus.randomuser.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Entity(tableName = "user_table",indices = [Index(value = ["name"], unique = true)])
data class Result(

    @ColumnInfo(name = "name") val name: String,
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


