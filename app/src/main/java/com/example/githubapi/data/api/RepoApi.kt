package com.example.githubapi.data.api

import com.example.githubapi.data.model.RepoModel
import com.example.githubapi.ui.model.RepoSearchResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RepoApi {
    // repo 검색
    @GET("search/repositories")
    fun searchRepository(@Query("q") query: String) : Single<RepoSearchResponse>

    // repo의 정보 검색
    @GET("repos/{owner}/{name}")
    fun getRepository(
        @Path("owner") ownerLogin : String,
        @Path("name") repoName : String
    ) : Single<RepoModel>

    // 서버 통신으로 받아올 데이터는 오직 1개만 있으면 된다. 따라서 1개의 데이터만 발행하는 Observable인 Single을 사용함
}