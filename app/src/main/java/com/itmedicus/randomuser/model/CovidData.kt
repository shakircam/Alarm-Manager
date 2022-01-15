package com.itmedicus.randomuser.model

import java.util.*

data class CovidData(
    val dateChecked: Date,
    val negativeIncrease: Int,
    val positiveIncrease: Int,
    val deathIncrease: Int,
    val state : String
)
