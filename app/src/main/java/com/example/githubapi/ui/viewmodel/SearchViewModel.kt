package com.example.githubapi.ui.viewmodel

import android.content.Context
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.example.githubapi.R
import com.example.githubapi.data.BaseResponse
import com.example.githubapi.data.model.mapToPresentation
import com.example.githubapi.repository.RepoRepository
import com.example.githubapi.ui.model.RepoItem
import com.example.githubapi.ui.model.RepoSearchResponse
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class SearchViewModel(private val searchRepoRepository: RepoRepository, private val compositeDisposable: CompositeDisposable) {
    val isLoading = ObservableField(false)
    val enabledSearchButton = ObservableField(true)
    val errorMessage = ObservableField("")
    val editSearchText = ObservableField("")
    val items = ObservableField<List<RepoItem>>(emptyList())

    init {
        editSearchText.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                // 사용자가 입력한 값을 관찰하고 있어 실시간으로 데이터를 받아올 수 있음
                val query = editSearchText.get()
                if(query.isNullOrEmpty()) {
                    enabledSearchButton.set(false)
                }
                else {
                    enabledSearchButton.set(true)
                }
            }
        })
    }

    fun searchRepository(context: Context, query : String) {
        searchRepoRepository.searchRepository(query, object : BaseResponse<RepoSearchResponse> {
            override fun onSuccess(data: RepoSearchResponse) {
                items.set(data.items.map { it.mapToPresentation(context) })

                if(0 == data.totalCount) {
                    showErrorMessage(context.getString(R.string.no_search_result))
                }
            }

            override fun onFail(description: String) {
                showErrorMessage(description)
            }

            override fun onError(throwable: Throwable) {
                showErrorMessage(throwable.message ?: context.getString(R.string.unexpected_error) )
            }

            override fun onLoading() {
                clearItems()
                showLoading()
                hideErrorMessage()
            }

            override fun onLoaded() {
                hideLoading()
            }
        }).also {
            compositeDisposable.add(it)
        }
    }

    private fun clearItems() {
        items.set(emptyList())
    }
    private fun showLoading() {
        isLoading.set(true)
    }
    private fun hideLoading() {
        isLoading.set(false)
    }
    private fun showErrorMessage(error : String) {
        errorMessage.set(error)
    }
    private fun hideErrorMessage() {
        errorMessage.set("")
    }
}