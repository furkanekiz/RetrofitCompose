package com.furkanekiz.retrofitcompose.service

import com.furkanekiz.retrofitcompose.model.CryptoModel
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {

    @GET("users/{user}/repos")
    fun getData(): Call<List<CryptoModel>>
}