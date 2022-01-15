package com.itmedicus.randomuser.data.network


import com.itmedicus.randomuser.model.News
import com.itmedicus.randomuser.utils.Constants.Companion.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {


    @GET("recent?")
    fun getNews(@Query("country")country : String = "bd",@Query("apiKey")apiKey : String = API_KEY) : Call<News>
}