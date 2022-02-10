package com.example.githubapi.repository

import com.example.githubapi.data.BaseResponse
import com.example.githubapi.data.model.RepoDetailModel
import com.example.githubapi.ui.model.RepoSearchResponse

interface RepoRepository {
    fun searchRepository(query: String, callback : BaseResponse<RepoSearchResponse>)
    fun getDetailRepository(user : String, repo: String, callback : BaseResponse<RepoDetailModel>)
}