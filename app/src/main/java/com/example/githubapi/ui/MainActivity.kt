package com.example.githubapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubapi.R
import com.example.githubapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initTitle()
        initMainFragment()
        System.out.println("MainActivity : onCreate")
    }

    private fun initTitle() {
        title = getString(R.string.search_repository)
    }
    private fun initMainFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(binding.FragmentContainer.id, SearchFragment.newInstance())
            .commit()
        System.out.println("MainActivity : initFragment")
    }
    fun goToDetailFragment(ownerName : String, repoName : String) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.FragmentContainer.id, DetailFragment.newInstance(ownerName, repoName))
            .addToBackStack(null)
            .commit()
    }
}