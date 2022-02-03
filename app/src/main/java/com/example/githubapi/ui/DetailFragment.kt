package com.example.githubapi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.githubapi.R
import com.example.githubapi.data.api.ApiProvider
import com.example.githubapi.databinding.FragmentDetailBinding
import com.example.githubapi.ui.model.RepoModel
import com.example.githubapi.ui.model.UserModel
import retrofit2.Call

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
    private val repoCall : Call<RepoModel>? = null
    private val userCall : Call<UserModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }




}