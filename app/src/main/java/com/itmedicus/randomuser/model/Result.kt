package com.itmedicus.randomuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    @SerializedName("gender") val gender : String,
    @SerializedName("email") val email : String,
    @SerializedName("nat") val nat : String
): Parcelable
