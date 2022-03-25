package com.hadimas.githubapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hadimas.githubapp.adapter.SectionPagerAdapter
import com.hadimas.githubapp.databinding.ActivityDetailUserBinding
import com.hadimas.githubapp.viewmodel.DetailUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    companion object {
        const val EXTRA_USERNAME = "username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_STATE_THEMES = "extra_state_themes"
        var url: String = ""

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)
        val stateThemes = intent.getBooleanExtra(EXTRA_STATE_THEMES, false)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

//      memasukan data kedalam view
        showLoading(true)
        viewModel = ViewModelProvider(this).get(
            DetailUserViewModel::class.java
        )
        if (stateThemes) {
            binding.apply {
                icLocation.setImageResource(R.drawable.location_white)
                iconRepo.setImageResource(R.drawable.repo_white)
                icComp.setImageResource(R.drawable.company_white)
                icFollowing.setImageResource(R.drawable.people_white)
                iconFollowers.setImageResource(R.drawable.people_white)
                bgDetail.setBackgroundResource(R.drawable.bg_dark)
            }
        }
        viewModel.setUserDetail(username.toString())
        viewModel.getUserDetail().observe(this@DetailUserActivity) {
            if (it != null) {
                binding.apply {
                    detailUsername.text = getString(R.string.set_username, it.login)
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(detailAvatar)
                    if (it.name != null) {
                        detailName.text = it.name
                    } else {
                        detailName.text = it.login
                    }

                    if (it.location != null) {
                        detailLocation.text = it.location
                    } else {
                        detailLocation.text = getString(R.string.unknow)
                    }

                    tvRepo.text = getString(R.string.set_repo, it.publicRepos)
                    tvFollowers.text = getString(R.string.set_follower, it.followers)
                    tvFollowing.text = getString(R.string.set_following, it.following)
                    if (it.company != null) {
                        detailCompany.text = it.company
                    } else {
                        detailCompany.text = getString(R.string.not_in_company)
                    }
                    url = it.url
                }
                showLoading(false)
            }
        }

        var statusToggle = false
        CoroutineScope(Dispatchers.IO).launch {
            val cekUser = viewModel.cekUser(id)
            withContext(Dispatchers.Main) {
                if (cekUser != null) {
                    if (cekUser > 0) {
                        binding.favToggle.isChecked = true
                        statusToggle = true
                    } else {
                        binding.favToggle.isChecked = false
                        statusToggle = false
                    }
                }
            }
        }

        binding.favToggle.setOnClickListener {
            statusToggle = !statusToggle
            if (statusToggle) {
                if (avatarUrl != null) {
                    viewModel.addFavorit(username.toString(), id, avatarUrl)
                }
                Toast.makeText(this, "$username added to Favorite!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.delUserFav(id)
                Toast.makeText(this, "$username Deleted from Favorite!", Toast.LENGTH_SHORT).show()
            }
            binding.favToggle.isChecked = statusToggle
        }

//      mengkonfigurasi tablayout dan view pager followers dan following
        val sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
        supportActionBar!!.title = "Detail User"
    }

    //  fungsi menampilkan progress bar
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_menu -> {
                val intentShare = Intent(Intent.ACTION_SEND)
                intentShare.putExtra("Share This", url)
                intentShare.type = "text/plain"
                val chooser = Intent.createChooser(intentShare, "Share Using: ")
                startActivity(chooser)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}