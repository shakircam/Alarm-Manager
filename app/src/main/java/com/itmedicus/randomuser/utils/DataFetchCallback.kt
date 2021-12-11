package com.itmedicus.randomuser.utils

interface DataFetchCallback <T> {
    fun onSuccess(data : T)
    fun onError(throwable: Throwable)
}