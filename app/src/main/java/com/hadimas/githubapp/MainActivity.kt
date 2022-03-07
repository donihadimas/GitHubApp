package com.hadimas.githubapp

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hadimas.githubapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUser: RecyclerView
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rvUser = binding.rvListuser
        rvUser.setHasFixedSize(true)

        list.addAll(listUsers)
        showRecycleList()

    }
//menambahkan data kedalam listUser
    private val listUsers: ArrayList<User>
        get() {
            val dataUserName = resources.getStringArray(R.array.username)
            val dataName = resources.getStringArray(R.array.name)
            val dataRepo = resources.getStringArray(R.array.repository)
            val dataPhoto = resources.obtainTypedArray(R.array.avatar)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataFollower = resources.getStringArray(R.array.followers)
            val dataFollowing = resources.getStringArray(R.array.following)
            val dataCompany = resources.getStringArray(R.array.company)
            val listUser = ArrayList<User>()
            for (i in dataUserName.indices){
                val user = User(dataUserName[i], dataName[i], dataRepo[i] , dataPhoto.getResourceId(i, -1), dataLocation[i], dataCompany[i], dataFollower[i], dataFollowing[i]  )
                listUser.add(user)
            }
            return listUser
        }

//    fungsi menampilkan recycle list
    private fun showRecycleList()  {
        if(applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvUser.layoutManager = GridLayoutManager(this, 2)
        }else{
            rvUser.layoutManager = LinearLayoutManager(this)
        }
        val listUserAdapter = ListHeroAdapter(list)
        rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListHeroAdapter.OnItemClickCallback{
            override fun onItemClicked(data : User){
                val intentToDetail = Intent(this@MainActivity, DetailUser::class.java)
                intentToDetail.putExtra("DATA", data)
                startActivity(intentToDetail)
            }
        })
    }
}