package com.hadimas.githubapp.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hadimas.githubapp.api.ApiConfig
import com.hadimas.githubapp.local.DatabaseUser
import com.hadimas.githubapp.local.FavUserDao
import com.hadimas.githubapp.local.FavoritUsers
import com.hadimas.githubapp.response.DetailUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    val detuser = MutableLiveData<DetailUserResponse>()


    private var userDatabase: DatabaseUser? = DatabaseUser.getDatabase(application)
    private var userDao: FavUserDao? = userDatabase?.favUserDao()


    fun setUserDetail(username: String) {
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    detuser.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserDetail(): LiveData<DetailUserResponse> {
        return detuser
    }

    fun addFavorit(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoritUsers(
                username,
                id,
                avatarUrl
            )
            userDao?.addFavorit(user)
        }
    }

    suspend fun cekUser(id: Int) = userDao?.cekUser(id)

    fun delUserFav(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.delUserFav(id)
        }
    }
}