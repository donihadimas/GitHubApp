package com.hadimas.githubapp.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadimas.githubapp.api.ApiConfig
import com.hadimas.githubapp.response.UserItems
import com.hadimas.githubapp.response.UsersSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {
    val listSearchUsers = MutableLiveData<ArrayList<UserItems>>()

    fun setSearchUser(query: String ){
        val client = ApiConfig.getApiService().getSearchUsers(query)
        client.enqueue(object : Callback<UsersSearchResponse>{
            override fun onResponse(
                call: Call<UsersSearchResponse>,
                response: Response<UsersSearchResponse>
            ) {
                if (response.isSuccessful){
                    listSearchUsers.postValue(response.body()?.items)
                }
            }
            override fun onFailure(call: Call<UsersSearchResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getSearchUser(): LiveData<ArrayList<UserItems>>{
        return listSearchUsers
    }


}