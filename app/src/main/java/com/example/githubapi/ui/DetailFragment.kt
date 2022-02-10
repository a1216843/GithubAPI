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
import com.example.githubapi.data.api.ApiProvider
import com.example.githubapi.data.model.RepoModel
import com.example.githubapi.data.model.UserModel
import com.example.githubapi.data.model.mapToPresentation
import com.example.githubapi.databinding.FragmentDetailBinding
import com.example.githubapi.ui.model.*
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

    private val repoApi = ApiProvider.RepoApi
    private val userApi = ApiProvider.UserApi
    private var repoCall : Call<RepoModel>? = null
    private var userCall : Call<UserModel>? = null

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

        loadRepoData(ownerName, repo)
        loadUserData(ownerName)
    }

    private fun loadRepoData(ownerName: String, repo : String) {
        hideProgress()
        showProgress()

        repoCall = repoApi.getRepository(ownerName, repo)
        repoCall?.enqueue(object : Callback<RepoModel> {
            override fun onResponse(call: Call<RepoModel>, response: Response<RepoModel>) {
                hideProgress()

                val body = response.body()
                if(response.isSuccessful && null != body) {
                    setRepoData(body.mapToPresentation(requireContext()))
                }
                else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<RepoModel>, t: Throwable) {
                hideProgress()
                showError(t.message)
            }
        })
    }

    private fun setRepoData(repoItem: RepoItem) {
        Glide.with(requireContext())
            .load(repoItem.owner.ownerUrl)
            .placeholder(ColorDrawable(Color.GRAY))
            .error(ColorDrawable(Color.RED))
            .into(binding.ivProfile)

        binding.tvTitle.text = repoItem.title
        binding.tvStars.text = repoItem.stars
        binding.tvDescription.text = repoItem.description
        binding.tvLanguage.text = repoItem.language
    }

    private fun loadUserData(userName : String){
        userCall = userApi.getUser(userName)
        userCall?.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                hideProgress()

                val body = response.body()
                if(response.isSuccessful && null != body) {
                    setUserData(body.mapToPresentation(requireContext()))
                }
                else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                hideProgress()
                showError(t.message)
            }
        })
    }

    private fun setUserData(userItem: UserItem) {
        binding.tvFollower.text = userItem.followers
        binding.tvFollowing.text = userItem.following
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
        repoCall?.cancel()
        userCall?.cancel()
        super.onStop()
    }




}