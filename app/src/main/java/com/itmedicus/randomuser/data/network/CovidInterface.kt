package com.itmedicus.randomuser.data.network

import com.itmedicus.randomuser.model.CovidData
import retrofit2.Call
import retrofit2.http.GET

interface CovidInterface {

    @GET("us/daily.json")
    fun getNationalData(): Call<List<CovidData>>

    @GET("states/daily.json")
    fun getStatesData(): Call<List<CovidData>>
}