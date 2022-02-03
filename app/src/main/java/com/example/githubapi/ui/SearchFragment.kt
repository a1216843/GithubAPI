package com.example.githubapi.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.githubapi.R
import com.example.githubapi.data.api.ApiProvider
import com.example.githubapi.databinding.FragmentDetailBinding
import com.example.githubapi.databinding.FragmentSearchBinding
import com.example.githubapi.ui.adapter.RepositoryAdapter
import com.example.githubapi.ui.model.RepoSearchResponse
import com.example.githubapi.ui.model.mapToPresentation
import com.example.githubapi.utils.AppUtils
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
        System.out.println("SearchFragment : onCreateView")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        initEditText()
        initButton()
        System.out.println("SearchFragment : onActivityCreated")
    }
    private fun initRecyclerView() {
        binding.listSearchRepository.adapter = repoAdapter
    }
    private fun initEditText() {
        binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
               when(actionId) {
                   EditorInfo.IME_ACTION_SEARCH -> {
                       val query = view?.text.toString()
                       searchRepository(query)
                       return true
                   }
                   else -> {
                       return false
                   }
               }
            }
        })
    }
    private fun initButton() {
        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            searchRepository(query)
        }
    }

    private fun searchRepository(query : String) {
        if(query.isEmpty()) {
            Toast.makeText(activity, R.string.please_write_repository_name, Toast.LENGTH_SHORT).show()
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
                            setItems(body.items.map{ it.mapToPresentation(requireContext()) })
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

    private fun hideKeyboard() {
        AppUtils.hideSoftKeyBoard(requireActivity())
    }
    private fun clearResults() {
        repoAdapter.clearItems()
    }
    private fun showProgress() {
        binding.pbLoading.visibility = View.VISIBLE
    }
    private fun hideProgress() {
        binding.pbLoading.visibility = View.GONE
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

    override fun onStop() {
        repoCall?.cancel()
        super.onStop()
    }


}