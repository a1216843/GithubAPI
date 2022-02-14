package com.example.githubapi.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.githubapi.R
import com.example.githubapi.data.BaseResponse
import com.example.githubapi.data.api.ApiProvider
import com.example.githubapi.data.model.RepoDetailModel
import com.example.githubapi.data.model.RepoModel
import com.example.githubapi.data.model.UserModel
import com.example.githubapi.data.model.mapToPresentation
import com.example.githubapi.databinding.FragmentDetailBinding
import com.example.githubapi.repository.RepoRepository
import com.example.githubapi.repository.RepoRepositoryImpl
import com.example.githubapi.ui.model.*
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException

class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    companion object {
        private const val ARGUMENT_OWNER_NAME = "owner_name"
        private const val ARGUMENT_REPO = "repo"
        fun newInstance(ownerName : String, repoName : String) = DetailFragment().apply {
            arguments = bundleOf(
                Pair(ARGUMENT_OWNER_NAME ,ownerName),
                Pair(ARGUMENT_REPO, repoName)
            )
        }
    }

    private val compositeDisposable = CompositeDisposable()
    private val repoRepository : RepoRepository = RepoRepositoryImpl(ApiProvider.RepoApi, ApiProvider.UserApi)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        System.out.println("DetailFragment : onCreateView")
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val ownerName = arguments?.getString(ARGUMENT_OWNER_NAME) ?: throw IllegalArgumentException(
            "No owner name info exists in extras"
        )
        val repo = arguments?.getString(ARGUMENT_REPO) ?: throw IllegalArgumentException(
            "No repo info exists in extras"
        )
        getDetailRepository(ownerName, repo)
    }

    private fun getDetailRepository(ownerName: String, repo : String) {
        repoRepository.getDetailRepository(ownerName, repo, object : BaseResponse<RepoDetailModel> {
            override fun onSuccess(data: RepoDetailModel) {
                if(null == data) {
                    showError("No search result")
                }
                setItem(data.mapToPresentation(requireContext()))
            }

            override fun onFail(description: String) {
                showError(description)
            }

            override fun onError(throwable: Throwable) {
                showError(throwable.message)
            }

            override fun onLoading() {
                hideError()
                showProgress()
            }

            override fun onLoaded() {
                hideProgress()
            }

        }).also {
            compositeDisposable.add(it)
        }
    }

    private fun setItem(repoDetailItem: RepoDetailItem) {
        Glide.with(requireContext())
            .load(repoDetailItem.ownerUrl)
            .placeholder(ColorDrawable(Color.GRAY))
            .error(ColorDrawable(Color.RED))
            .into(binding.ivProfile)

        binding.tvTitle.text = repoDetailItem.title
        binding.tvStars.text = repoDetailItem.stars
        binding.tvDescription.text = repoDetailItem.description
        binding.tvLanguage.text = repoDetailItem.language
        binding.tvFollower.text = repoDetailItem.followers
        binding.tvFollowing.text = repoDetailItem.following
    }



    private fun showError(message : String?) {
        with(binding.tvMessage) {
            text = message ?: context.getString(R.string.unexpected_error)
            visibility = View.VISIBLE
        }
    }
    private fun hideError() {
        with(binding.tvMessage) {
            text = ""
            visibility = View.GONE
        }
    }
    private fun showProgress() {
        binding.tvMessage.visibility = View.GONE
        binding.pbLoading.visibility = View.VISIBLE
    }
    private fun hideProgress() {
        binding.pbLoading.visibility = View.GONE
    }

    override fun onStop() {
        compositeDisposable.dispose()
        super.onStop()
    }




}