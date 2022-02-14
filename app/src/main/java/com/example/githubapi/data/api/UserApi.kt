package com.example.githubapi.data.api

import com.example.githubapi.data.model.UserModel
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users/{name}")
    fun getUser(@Path("name") userName : String) : Single<UserModel>
}