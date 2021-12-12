package com.itmedicus.randomuser.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itmedicus.randomuser.data.repository.UserListRepository
import com.itmedicus.randomuser.json.Dami
import com.itmedicus.randomuser.model.User
import com.itmedicus.randomuser.utils.DataFetchCallback
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserListRepository): ViewModel() {

    val userListLiveData = MutableLiveData<MutableList<Dami.Results>>()
    val userListFailedLiveData = MutableLiveData<String>()

    fun getUserList() {

        viewModelScope.launch { repository.getCategoriesList(object :
            DataFetchCallback<MutableList<Dami.Results>> {
            override fun onSuccess(data: MutableList<Dami.Results>) {
                userListLiveData.postValue(data)
            }

            override fun onError(throwable: Throwable) {
                userListFailedLiveData.postValue(throwable.localizedMessage)
            }

        }) }

    }
}