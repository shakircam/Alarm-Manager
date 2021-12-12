package com.itmedicus.randomuser.data.repository

import com.itmedicus.randomuser.json.Dami
import com.itmedicus.randomuser.model.User
import com.itmedicus.randomuser.utils.DataFetchCallback

interface UserListRepository {
    suspend fun getCategoriesList(callback: DataFetchCallback<MutableList<Dami.Results>>)
}