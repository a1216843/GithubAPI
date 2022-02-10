package com.example.githubapi.ui.model

import com.example.githubapi.data.model.RepoModel
import com.google.gson.annotations.SerializedName

data class RepoSearchResponse(
    @SerializedName("total_count")
    val totalCount : Int,
    @SerializedName("items")
    val items : List<RepoModel>
)
