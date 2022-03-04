package com.hadimas.githubapp

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

    private val listUsers: ArrayList<User>
        get() {
            val dataUserName = resources.getStringArray(R.array.username)
            val dataName = resources.getStringArray(R.array.name)
            val dataCompany = resources.getStringArray(R.array.company)
            val dataPhoto = resources.obtainTypedArray(R.array.avatar)
            val listUser = ArrayList<User>()
            for (i in dataUserName.indices){
                val user = User(dataUserName[i], dataName[i], dataCompany[i], dataPhoto.getResourceId(i, -1))
                listUser.add(user)
            }
            return listUser
        }

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
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user : User){
        Toast.makeText(this, "Item Selected : " + user.username, Toast.LENGTH_SHORT).show()
    }
}