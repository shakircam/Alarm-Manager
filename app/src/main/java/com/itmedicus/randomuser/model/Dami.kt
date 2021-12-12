package com.itmedicus.randomuser.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


data class Dami(
    val info: Info,
    val results: List<Results>
){


    data class Results(
        val email: String,
        val gender: String,
        val nat: String,
        val phone : String,
        val name: Name,
        val picture: Picture,
        val location: Location,
    )

    data class Name(
        val first: String,
        val last: String,
        val title: String
    )

    data class Picture(
        val large: String,
        val medium: String,
        val thumbnail: String
    )

    data class Location(
        val city: String,
        val postcode: String,
        val state: String
    )
}
