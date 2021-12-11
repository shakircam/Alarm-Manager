package com.itmedicus.randomuser.data.repository

import com.itmedicus.randomuser.model.User
import com.itmedicus.randomuser.utils.DataFetchCallback

interface UserListRepository {
    suspend fun getCategoriesList(callback: DataFetchCallback<MutableList<User>>)
}