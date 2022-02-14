package com.example.githubapi.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
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
import com.example.githubapi.ui.viewmodel.DetailViewModel
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
    private val detailModel by lazy {
        DetailViewModel(repoRepository, compositeDisposable)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        System.out.println("DetailFragment : onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.model = detailModel
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
        detailModel.getDetailRepository(requireContext(), ownerName, repo)
        initObserve()
    }

    private fun initObserve() {
        detailModel.item.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                detailModel.item.get()?.let {
                    setItem(it)
                }
            }
        })
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

    override fun onStop() {
        compositeDisposable.dispose()
        super.onStop()
    }




}