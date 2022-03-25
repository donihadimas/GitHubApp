package com.hadimas.githubapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hadimas.githubapp.adapter.ListSearchUserAdapter
import com.hadimas.githubapp.local.DatabaseUser
import com.hadimas.githubapp.local.FavUserDao
import com.hadimas.githubapp.local.FavoritUsers

class FavoritViewModel(application: Application) : AndroidViewModel(application) {
    private var userDao: FavUserDao?
    private var userDatabase: DatabaseUser? = DatabaseUser.getDatabase(application)

    init {
        userDao = userDatabase?.favUserDao()
    }

    fun getFavUsers(): LiveData<List<FavoritUsers>>? {
        return userDao?.getFavUsers()
    }
}