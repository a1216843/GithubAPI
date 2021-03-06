package com.example.githubapi.ui.model

data class RepoItem(
    val title : String,
    val repoName : String,
    val owner : OwnerItem,
    val description : String?,
    val language : String?,
    val updateAt : String,
    val stars : String
) {
    data class OwnerItem(
        val ownerName : String,
        val ownerUrl : String
    )
}
