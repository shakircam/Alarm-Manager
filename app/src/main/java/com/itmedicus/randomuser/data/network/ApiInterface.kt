package com.itmedicus.randomuser.data.network

import com.itmedicus.randomuser.json.Dami
import com.itmedicus.randomuser.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("?results=50")
    fun getRandomUserr() : Call<Dami>

    @GET("?results=5")
    fun getRandomUser() : Call<MutableList<Dami.Results>>

    @GET("?results=5")
    fun getUser() : Response<Dami.Results>
}