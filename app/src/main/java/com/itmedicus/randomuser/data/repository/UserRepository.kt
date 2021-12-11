package com.itmedicus.randomuser.data.repository

import com.itmedicus.randomuser.data.network.ApiInterface
import com.itmedicus.randomuser.data.network.RetrofitClient
import com.itmedicus.randomuser.model.User
import com.itmedicus.randomuser.utils.DataFetchCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserRepository : UserListRepository{

    override suspend fun getCategoriesList(callback: DataFetchCallback<MutableList<User>>) {
        val apiInterface = RetrofitClient.getClient().create(ApiInterface::class.java)
        val call = apiInterface.getRandomUser()

        call.enqueue(object : Callback<MutableList<User>> {

            override fun onResponse(
                call: Call<MutableList<User>>,
                response: Response<MutableList<User>>
            ) {
                response.body()?.let {
                    callback.onSuccess(it)
                }
            }

            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                callback.onError(t)
            }

        })
    }
}