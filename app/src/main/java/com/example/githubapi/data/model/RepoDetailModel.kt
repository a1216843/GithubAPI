package com.example.githubapi.data.model

import android.content.Context
import android.text.TextUtils
import android.text.format.DateUtils
import com.example.githubapi.R
import com.example.githubapi.ui.model.RepoDetailItem
import com.example.githubapi.utils.DateUtils.dateFormatToShow
import java.lang.IllegalArgumentException
import java.util.*

data class RepoDetailModel(
    val title : String,
    val repoName : String,
    val ownerName : String,
    val ownerUrl : String,
    val followers : Int,
    val following : Int,
    val description : String?,
    val language : String?,
    val updateAt : Date,
    val stars : Int
)

fun RepoDetailModel.mapToPresentation(context : Context) = RepoDetailItem(
    title = title,
    repoName = repoName,
    ownerName = ownerName,
    ownerUrl = ownerUrl,
    followers = followers.let {
        if(it > 100) context.getString(R.string.max_follow_number)
        else it.toString()
    },
    following = following.let {
        if(it > 100) context.getString(R.string.max_follow_number)
        else it.toString()
    },
    description =
    if(TextUtils.isEmpty(description)) context.resources.getString(R.string.no_description_provided)
    else description!!,
    language = if(TextUtils.isEmpty(language)) context.resources.getString(R.string.no_language_specified)
    else language!!,
    updateAt = try {
        com.example.githubapi.utils.DateUtils.dateFormatToShow.format(updateAt)
    } catch (e : IllegalArgumentException) {
        context.resources.getString(R.string.unknown)
    },
    stars = context.resources.getQuantityString(R.plurals.star, stars, stars)
)
