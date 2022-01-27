package com.example.githubapi.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiProvider {
    private const val baseUrl = "https://api.github.com/"
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val UserApi = retrofit.create(UserApi::class.java)
    val RepoApi = retrofit.create(RepoApi::class.java)

}