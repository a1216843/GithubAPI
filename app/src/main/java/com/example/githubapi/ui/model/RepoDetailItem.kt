package com.example.githubapi.ui.model

data class RepoDetailItem(
    val title : String,
    val repoName : String,
    val ownerName : String,
    val ownerUrl : String,
    val followers : String,
    val following : String,
    val description : String,
    val language : String,
    val updateAt : String,
    val stars : String
)
