package com.hadimas.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hadimas.githubapp.databinding.ActivityDetailUserBinding


class DetailUser : AppCompatActivity() {
    private lateinit var binding : ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Menerima data user
        val data = intent.getParcelableExtra<User>("DATA")

        //Set Data
        val photo = data!!.avatar
        val username = "@${data.username}"
        val repo = "${data.repo} repository not shown"
        binding.apply {
            detailUsername.text = username
            detailAvatar.setImageResource(photo)
            detailName.text = data.name
            detailLocation.text = data.location
            detailCompany.text = data.company
            follower.text = data.follower
            following.text = data.following
            repos.text = repo
            btnFollow.setOnClickListener{
                Toast.makeText(btnFollow.context,"Sorry, not configured", Toast.LENGTH_SHORT).show()
            }
        }
//        Mengganti Title Action Bar
        supportActionBar?.title = "Detail User"


    }
}