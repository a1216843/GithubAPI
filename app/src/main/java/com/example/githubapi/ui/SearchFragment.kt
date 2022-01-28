package com.example.githubapi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.githubapi.R
import com.example.githubapi.data.api.ApiProvider
import com.example.githubapi.databinding.FragmentDetailBinding
import com.example.githubapi.databinding.FragmentSearchBinding
import com.example.githubapi.ui.adapter.RepositoryAdapter
import com.example.githubapi.ui.model.RepoSearchResponse
import com.example.githubapi.ui.model.mapToPresentation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    lateinit var binding : FragmentSearchBinding
    companion object {
        fun newInstance() = SearchFragment()
    }

    // TODO : RepoAdapter
    private val repoAdapter by lazy {
        RepositoryAdapter().apply{
            onItemClick = {
                (requireActivity() as MainActivity).goToDetailFragment(
                    it.owner.ownerName,
                    it.repoName
                )
            }
        }
    }
    private val repoApi = ApiProvider.RepoApi
    private var repoCall : Call<RepoSearchResponse>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun searchRepository(query : String) {
        if(query.isEmpty()) {
            Toast.makeText(activity, R.string.please_write_repository_name)
        }
        else {
            hideKeyboard()
            clearResults()
            hideError()
            showProgress()

            repoCall = repoApi.searchRepository(query)
            repoCall?.enqueue(object : Callback<RepoSearchResponse> {
                override fun onResponse(
                    call: Call<RepoSearchResponse>,
                    response: Response<RepoSearchResponse>
                ) {
                    hideProgress()

                    val body = response.body()
                    if(response.isSuccessful && null != body) {
                        with(repoAdapter){
                            setItem(body.items.map{ it.mapToPresentation(requireContext()) })
                        }

                        if(0 == body.totalCount) {
                            showError(getString(R.string.no_search_result))
                        }
                    }
                    else {
                        showError(response.message())
                    }
                }

                override fun onFailure(call: Call<RepoSearchResponse>, t: Throwable) {
                    hideProgress()
                    showError(t.message)
                }
            })
        }
    }


}