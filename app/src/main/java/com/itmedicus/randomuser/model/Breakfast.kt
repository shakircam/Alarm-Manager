package com.itmedicus.randomuser.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breakfast_table")
data class Breakfast(

    var title: String,
    var kcal: Int,
    var image: String,
    var carbs: Int,
    var protein: Int,
    var fat: Int
){
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}
