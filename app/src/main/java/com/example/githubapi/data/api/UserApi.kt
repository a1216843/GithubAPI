package com.example.githubapi.data.api

import com.example.githubapi.ui.model.UserModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users/")
    fun getUser(@Path("name") userName : String) : Call<UserModel>
}