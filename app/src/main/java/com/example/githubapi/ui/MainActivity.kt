package com.example.githubapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubapi.R
import com.example.githubapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        System.out.println("MainActivity : onCreate")
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initTitle()
        initMainFragment()

    }

    private fun initTitle() {
        title = getString(R.string.search_repository)
    }
    private fun initMainFragment() {
        System.out.println("MainActivity : initFragment")
        supportFragmentManager
            .beginTransaction()
            .add(binding.FragmentContainer.id, SearchFragment.newInstance())
            .commit()
    }
    fun goToDetailFragment(ownerName : String, repoName : String) {
        System.out.println("MainActivity : goToDetailFragment")
        supportFragmentManager
            .beginTransaction()
            .replace(binding.FragmentContainer.id, DetailFragment.newInstance(ownerName, repoName))
            .addToBackStack(null)
            .commit()
    }
}