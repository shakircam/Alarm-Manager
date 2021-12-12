package com.itmedicus.randomuser.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Entity(tableName = "user_table")
data class Result(
    @SerializedName("gender") val gender : String,
    @SerializedName("email") val email : String,
    @SerializedName("nat") val nat : String,
    val phone : String
){
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}


