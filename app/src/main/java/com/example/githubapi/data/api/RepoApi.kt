package com.example.githubapi.data.api

import com.example.githubapi.ui.model.RepoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RepoApi {
    // repo 검색
    @GET("search/repositories")
    fun searchRepository(@Query("q") query: String) : Call<RepoSeachResponse>

    // repo의 정보 검색
    @GET("repos//")
    fun getRepository(
        @Path("owner") ownerLogin : String,
        @Path("name") repoName : String
    ) : Call<RepoModel>
}