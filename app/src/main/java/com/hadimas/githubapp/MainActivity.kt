package com.hadimas.githubapp

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadimas.githubapp.adapter.ListSearchUserAdapter
import com.hadimas.githubapp.databinding.ActivityMainBinding
import com.hadimas.githubapp.response.UserItems
import com.hadimas.githubapp.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapterSearchUser : ListSearchUserAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapterSearchUser = ListSearchUserAdapter()
        adapterSearchUser.notifyDataSetChanged()
//mengirim data
        adapterSearchUser.setOnItemClickCallback(object : ListSearchUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserItems) {
                val intentToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                intentToDetail. putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                startActivity(intentToDetail)
            }

        })
        binding.apply {
            rvListuser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvListuser.setHasFixedSize(true)
            rvListuser.adapter = adapterSearchUser
        }

        viewModel.getSearchUser().observe(this) {
            if (it != null) {
                adapterSearchUser.setList(it)
                showLoading(false)
                binding.icBlackMain.visibility = View.GONE
            }
        }
    }
    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
//search username
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                viewModel.setSearchUser(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }
}