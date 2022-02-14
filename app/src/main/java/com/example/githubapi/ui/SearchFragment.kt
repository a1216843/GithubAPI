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
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
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
import com.example.githubapi.ui.viewmodel.SearchViewModel
import com.example.githubapi.utils.AppUtils
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
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

    private val repoRepository : RepoRepository = RepoRepositoryImpl(ApiProvider.RepoApi, ApiProvider.UserApi)

    private val searchModel by lazy {
        SearchViewModel(repoRepository, compositeDisposable)
    }

    private lateinit var binding : FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.model = searchModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        System.out.println("SearchFragment : onActivityCreated")
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        initEditText()
        initObserve()
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
                       searchModel.searchRepository(requireContext(), query)
                       return true
                   }
                   else -> {
                       return false
                   }
               }
            }
        })
    }
    private fun initObserve() {
        searchModel.items.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                searchModel.items.get()?.let {
                    repoAdapter.setItems(it)
                }
            }
        })
    }

    override fun onStop() {
        // 만약 통신중일 때 화면이 닫힌다면 통신 또한 종료되어야 한다.
        // 따라서 onStop 메서드가 호출될 때 CompositeDisposable에 있는 객체들을 모두 dispose하여 메모리 누수, 불필요한 데이터 호출등을 막을 수 있다.
        compositeDisposable.dispose()
        super.onStop()
    }


}
