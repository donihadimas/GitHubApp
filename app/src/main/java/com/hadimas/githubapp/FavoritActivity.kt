package com.hadimas.githubapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadimas.githubapp.adapter.ListSearchUserAdapter
import com.hadimas.githubapp.databinding.ActivityFavoritBinding
import com.hadimas.githubapp.local.FavoritUsers
import com.hadimas.githubapp.response.UserItems
import com.hadimas.githubapp.viewmodel.FavoritViewModel

class FavoritActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritBinding
    private lateinit var adapterFavoritUsers : ListSearchUserAdapter
    private lateinit var viewmodel: FavoritViewModel

    companion object{
        const val EXTRA_STATE_THEMES = "extra_state_themes"
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val stateThemes = intent.getBooleanExtra(EXTRA_STATE_THEMES, false)

        if (stateThemes){
            binding.bgFav.setBackgroundResource(R.drawable.bg_dark)
        }
        adapterFavoritUsers =  ListSearchUserAdapter()
        adapterFavoritUsers.notifyDataSetChanged()

        viewmodel = ViewModelProvider(this).get(FavoritViewModel::class.java)
        adapterFavoritUsers.setOnItemClickCallback(object : ListSearchUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserItems) {
                val intentToDetail = Intent(this@FavoritActivity, DetailUserActivity::class.java)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_URL, data.avatarUrl)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_STATE_THEMES, stateThemes)
                startActivity(intentToDetail)
            }

        })
        binding.apply {
            rvListuser.layoutManager = LinearLayoutManager(this@FavoritActivity)
            rvListuser.setHasFixedSize(true)
            rvListuser.adapter = adapterFavoritUsers
        }

        viewmodel.getFavUsers()?.observe(this) {
            if (it != null) {
                val list = mapList(it)
                adapterFavoritUsers.setList(list)
                binding.apply {
                    favEmpty.visibility = View.GONE
                    tvEmpty.visibility = View.GONE
                }
            }
        }
        supportActionBar!!.title = "Favorite User"
    }

    private fun mapList(users: List<FavoritUsers>): ArrayList<UserItems> {
        val listUsers = ArrayList<UserItems>()
        for(idx in users){
            val userMapped = UserItems(
                idx.login,
                idx.id,
                idx.avatar_url
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}