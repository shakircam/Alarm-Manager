package com.itmedicus.randomuser.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itmedicus.randomuser.data.repository.UserListRepository
import com.itmedicus.randomuser.model.User
import com.itmedicus.randomuser.utils.DataFetchCallback
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserListRepository): ViewModel() {

    val userListLiveData = MutableLiveData<MutableList<User>>()
    val userListFailedLiveData = MutableLiveData<String>()

    fun getUserList() {

        viewModelScope.launch { repository.getCategoriesList(object :
            DataFetchCallback<MutableList<User>> {
            override fun onSuccess(data: MutableList<User>) {
                userListLiveData.postValue(data)
            }

            override fun onError(throwable: Throwable) {
                userListFailedLiveData.postValue(throwable.localizedMessage)
            }

        }) }

    }
}