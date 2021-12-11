package com.itmedicus.randomuser.data.network

import com.itmedicus.randomuser.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("?results=3")
    fun getRandomUser() : Call<MutableList<User>>
}