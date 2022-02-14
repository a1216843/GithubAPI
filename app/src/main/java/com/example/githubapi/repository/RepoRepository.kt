package com.example.githubapi.repository

import com.example.githubapi.data.BaseResponse
import com.example.githubapi.data.model.RepoDetailModel
import com.example.githubapi.ui.model.RepoSearchResponse
import io.reactivex.disposables.Disposable

interface RepoRepository {
    // Disposable은 Observer가 Observable을 구독할 때 생성되는 객체로, Observable에서 만드는 이벤트 스트림과 이에 필요한 리소스를 관리한다.
    // Observable로부터 더이상 이벤트를 받지 않으려면 Disposable을 통해 구독 해제를 할 수 있다.
    // 구독시 생성되는 Disposable 객체를 하나씩 관리할 수도 있지만, CompositeDisposable을 사용하면 여러 개의 Disposable 객체를 하나의 객체에서 관리할 수 있다.
    fun searchRepository(query: String, callback : BaseResponse<RepoSearchResponse>) : Disposable
    fun getDetailRepository(user : String, repo: String, callback : BaseResponse<RepoDetailModel>) : Disposable
}