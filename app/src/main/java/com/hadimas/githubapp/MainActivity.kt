package com.hadimas.githubapp

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.switchmaterial.SwitchMaterial
import com.hadimas.githubapp.adapter.ListSearchUserAdapter
import com.hadimas.githubapp.databinding.ActivityMainBinding
import com.hadimas.githubapp.response.UserItems
import com.hadimas.githubapp.viewmodel.MainViewModel
import com.hadimas.githubapp.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapterSearchUser: ListSearchUserAdapter
    private var stateToFavorit: Boolean = false

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapterSearchUser = ListSearchUserAdapter()
        adapterSearchUser.notifyDataSetChanged()
        val switchTheme = findViewById<SwitchMaterial>(R.id.themes_switcher)

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        mainViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }

        }
        val intentToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
            intentToDetail.putExtra(DetailUserActivity.EXTRA_STATE_THEMES, isChecked)
            binding.icBlackMain.setImageResource(R.drawable.people_search_white)
            binding.bgMain.setBackgroundResource(R.drawable.bg_dark)
            stateToFavorit = isChecked
        }

        adapterSearchUser.setOnItemClickCallback(object :
            ListSearchUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItems) {
                intentToDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_URL, data.avatarUrl)
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav_menu -> {
                val intentToFavorit = Intent(this, FavoritActivity::class.java)
                intentToFavorit.putExtra(FavoritActivity.EXTRA_STATE_THEMES, stateToFavorit)
                startActivity(intentToFavorit)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}