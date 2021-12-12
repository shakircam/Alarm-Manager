package com.itmedicus.randomuser.data.repository

import android.util.Log
import com.itmedicus.randomuser.data.network.ApiInterface
import com.itmedicus.randomuser.data.network.RetrofitClient
import com.itmedicus.randomuser.json.Dami
import com.itmedicus.randomuser.model.User
import com.itmedicus.randomuser.utils.DataFetchCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserRepository : UserListRepository{

    override suspend fun getCategoriesList(callback: DataFetchCallback<MutableList<Dami.Results>>) {
        val apiInterface = RetrofitClient.getClient().create(ApiInterface::class.java)
        val call = apiInterface.getRandomUser()

        call.enqueue(object : Callback<MutableList<Dami.Results>> {

            override fun onResponse(
                call: Call<MutableList<Dami.Results>>,
                response: Response<MutableList<Dami.Results>>
            ) {
                response.body()?.let {
                    callback.onSuccess(it)
                    val code = response.code()
                    Log.d("tag","$code")
                    Log.d("tag",it.toString())
                }
            }

            override fun onFailure(call: Call<MutableList<Dami.Results>>, t: Throwable) {
                callback.onError(t)
            }

        })
    }
}