package com.example.githubapi.repository

import com.example.githubapi.data.BaseResponse
import com.example.githubapi.data.api.RepoApi
import com.example.githubapi.data.api.UserApi
import com.example.githubapi.data.model.RepoDetailModel
import com.example.githubapi.data.model.RepoModel
import com.example.githubapi.data.model.UserModel
import com.example.githubapi.ui.model.RepoSearchResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class RepoRepositoryImpl(private val repoApi: RepoApi, private val userApi: UserApi) : RepoRepository {
    override fun searchRepository(query: String, callback: BaseResponse<RepoSearchResponse>) : Disposable {
        return repoApi.searchRepository(query)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                callback.onLoading()
            }
            .doOnTerminate {
                callback.onLoaded()
            }
            .subscribe({
                callback.onSuccess(it)
            }) {
                if(it is HttpException) {
                    callback.onFail(it.message())
                }
                else {
                    callback.onError(it)
                }
            }
    }

    override fun getDetailRepository(
        user: String,
        repo: String,
        callback: BaseResponse<RepoDetailModel>
    ): Disposable {
        return Single.zip(
            repoApi.getRepository(user, repo), userApi.getUser(user),
            BiFunction<RepoModel, UserModel, RepoDetailModel> { t1: RepoModel, t2: UserModel ->
                RepoDetailModel(
                    title = t1.fullName,
                    repoName = t1.name,
                    ownerName = t2.name,
                    ownerUrl = t2.profileImgUrl,
                    followers = t2.followers,
                    following = t2.following,
                    description = t1.description,
                    language = t1.language,
                    updateAt = t1.updateAt,
                    stars = t1.stars
                )
            }
        ).observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                callback.onLoading()
            }
            .doOnTerminate {
                callback.onLoaded()
            }
            .subscribe({
                callback.onSuccess(it)
            }) {
                if(it is HttpException) {
                    callback.onFail(it.message())
                    System.out.println("테스트 테스트 ${it.message}")
                }
                else {
                    callback.onError(it)
                    System.out.println("테스트 테스트1 ${it.message}")
                }
            }
    }
}