package com.example.githubapi.ui.viewmodel


import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.ObservableField
import com.example.githubapi.data.BaseResponse
import com.example.githubapi.data.model.RepoDetailModel
import com.example.githubapi.data.model.mapToPresentation
import com.example.githubapi.repository.RepoRepository
import com.example.githubapi.ui.model.RepoDetailItem
import io.reactivex.disposables.CompositeDisposable

class DetailViewModel(private val detailRepository: RepoRepository, private val compositeDisposable: CompositeDisposable)  {
    val isLoading = ObservableField(false)
    val errorMessage = ObservableField("")
    val item = ObservableField<RepoDetailItem>()

    fun getDetailRepository(context: Context, ownerName : String, repo : String) {
        detailRepository.getDetailRepository(ownerName, repo, object : BaseResponse<RepoDetailModel> {
            override fun onSuccess(data: RepoDetailModel) {
                if(null == data) {
                    showError("No search result")
                }
                else {
                    item.set(data.mapToPresentation(context))
                }
            }

            override fun onFail(description: String) {
                showError(description)
            }

            override fun onError(throwable: Throwable) {
                showError(throwable.message ?: "unexpected error")
            }

            override fun onLoading() {
                hideError()
                showLoading()
            }

            override fun onLoaded() {
                hideLoading()
            }
        }).also {
            compositeDisposable.add(it)
        }
    }

    private fun showError(error : String) {
        errorMessage.set(error)
    }
    private fun hideError() {
        errorMessage.set("")
    }
    private fun showLoading() {
        isLoading.set(true)
    }
    private fun hideLoading() {
        isLoading.set(false)
    }
}