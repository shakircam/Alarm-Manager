package com.itmedicus.randomuser.data.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitNewsClient {

    private const val BASE_URL = "https://www.apinetwork.xyz/"

    private val gson = GsonBuilder().setLenient().create()

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getNewsClient(): Retrofit {
        return retrofit
    }
}