package com.hadimas.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hadimas.githubapp.adapter.SectionPagerAdapter
import com.hadimas.githubapp.databinding.ActivityDetailUserBinding
import com.hadimas.githubapp.viewmodel.DetailUserViewModel


class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel
    companion object{
        const val EXTRA_USERNAME = "username"
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
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

//      memasukan data kedalam view
        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailUserViewModel::class.java)
        viewModel.setUserDetail(username.toString())
        viewModel.getUserDetail().observe(this@DetailUserActivity) {
            if (it != null) {
                binding.apply {
                    detailUsername.text = "@${it.login}"
                    Glide.with(this@DetailUserActivity)
                         .load(it.avatarUrl)
                        .centerCrop()
                        .into(detailAvatar)
                    if (it.name != null){
                        detailName.text = it.name
                    }else{
                        detailName.text = it.login
                    }

                    if(it.location != null){
                        detailLocation.text = it.location
                    }else{
                        detailLocation.text = "Unknow"
                    }

                    tvRepo.text = "${it.publicRepos} Repositories"
                    tvFollowers.text = "${it.followers} Followers"
                    tvFollowing.text = "${it.following} Following"
                    if(it.company != null ){
                        detailCompany.text = it.company
                    }else{
                        detailCompany.text = "Not in Company"
                    }
                }
                showLoading(false)
            }
        }

//      mengkonfigurasi tablayout dan view pager followers dan following
        val sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager){tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }
//  fungsi menampilkan progress bar
    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}