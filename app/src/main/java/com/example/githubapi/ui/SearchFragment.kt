package com.example.githubapi.ui

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapi.R
import com.example.githubapi.data.BaseResponse
import com.example.githubapi.data.api.ApiProvider
import com.example.githubapi.data.model.mapToPresentation
import com.example.githubapi.databinding.FragmentDetailBinding
import com.example.githubapi.databinding.FragmentSearchBinding
import com.example.githubapi.repository.RepoRepository
import com.example.githubapi.repository.RepoRepositoryImpl
import com.example.githubapi.ui.adapter.RepositoryAdapter
import com.example.githubapi.ui.model.RepoItem
import com.example.githubapi.ui.model.RepoSearchResponse
import com.example.githubapi.utils.AppUtils
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    lateinit var binding : FragmentSearchBinding
    companion object {
        fun newInstance() = SearchFragment()
    }

    private val compositeDisposable = CompositeDisposable()

    private val repoAdapter by lazy {
        RepositoryAdapter().apply{
            onItemClick = {
                System.out.println("ownerName : ${it.owner.ownerName}")
                (requireActivity() as MainActivity).goToDetailFragment(
                    it.owner.ownerName,
                    it.repoName
                )
            }
        }
    }


//    private val repoApi = ApiProvider.RepoApi
//    private var repoCall : Call<RepoSearchResponse>? = null
    // 사실 View에선 Data Source가 remote인지 local인지도 몰라야하므로 ApiProvider의 api를 매개변수로 넘겨주는 과정도 View에서 분리해야 함
    private val repoRepository : RepoRepository = RepoRepositoryImpl(ApiProvider.RepoApi, ApiProvider.UserApi)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        System.out.println("SearchFragment : onActivityCreated")
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        initEditText()
        initButton()
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
            repoRepository.searchRepository(query, object : BaseResponse<RepoSearchResponse> {
                override fun onSuccess(data: RepoSearchResponse) {
                    with(repoAdapter) {
                        setItems(data.items.map{ it.mapToPresentation(requireContext()) })
                    }
                    if (0 == data.totalCount) {
                        showError(getString(R.string.no_search_result))
                    }
                }

                override fun onFail(description: String) {
                    showError(description)
                }

                override fun onError(throwable: Throwable) {
                    showError(throwable.message)
                }

                override fun onLoading() {
                    hideKeyboard()
                    clearResults()
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
        // 만약 통신중일 때 화면이 닫힌다면 통신 또한 종료되어야 한다.
        // 따라서 onStop 메서드가 호출될 때 CompositeDisposable에 있는 객체들을 모두 dispose하여 메모리 누수, 불필요한 데이터 호출등을 막을 수 있다.
        compositeDisposable.dispose()
        super.onStop()
    }


}
