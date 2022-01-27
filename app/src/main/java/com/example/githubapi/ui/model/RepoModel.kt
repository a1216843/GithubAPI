package com.example.githubapi.ui.model

import android.content.Context
import android.text.TextUtils
import android.text.format.DateUtils
import com.example.githubapi.R
import com.google.gson.annotations.SerializedName
import java.lang.IllegalArgumentException
import java.util.*

data class RepoModel(
    @SerializedName("name")
    val name : String,
    @SerializedName("full_name")
    val fullName : String,
    @SerializedName("owner")
    val owner : OwnerModel,
    @SerializedName("description")
    val description : String?,
    @SerializedName("language")
    val language : String?,
    @SerializedName("update_at")
    val updateAt : Date,
    @SerializedName("stargazers_count")
    val stars : Int
) {
    data class OwnerModel(
        @SerializedName("login")
        val login : String,
        @SerializedName("avartar_url")
        val avatarUrl : String
    )
}

fun RepoModel.mapToPresentation(context : Context) = RepoItem(
    title = fullName,
    repoName = name,
    owner = RepoItem.OwnerItem(
        ownerName = owner.login,
        ownerUrl = owner.avatarUrl
    ),

    description = if(TextUtils.isEmpty(description))
        context.resources.getString(R.string.no_description_provided)
    else
        description,

    language = if(TextUtils.isEmpty(language))
        context.resources.getString(R.string.no_language_specified)
    else
        language,

    updateAt = try {
        TODO("시간으로 매핑해주기")
    } catch (e: IllegalArgumentException) {
        context.resources.getString(R.string.unknown)
    },

    // plurals는 수량과 관련한 문법적 규칙(단수, 복수)을 적용할 수 있도록 하는 기능으로
    // getQuantityString()의 첫 번째 매개변수는 plurals xml 리소스를 가져오고
    // 두 번째 매개변수는 수량을 통해 plurals 내에서 적절한 문법 규칙을 선택(단수인지 복수인지 등)
    // 세 번째 매개변수는 선택한 문법 규칙 내부(%d)에 들어감 ex. <item quantitiy="zero">%d개</item>
   stars = context.resources.getQuantityString(R.plurals.star, stars, stars))
