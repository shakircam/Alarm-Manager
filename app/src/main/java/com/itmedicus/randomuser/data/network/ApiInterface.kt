package com.itmedicus.randomuser.data.network


import com.itmedicus.randomuser.model.Dami
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("?results=50")
    fun getRandomUserr() : Call<Dami>

}