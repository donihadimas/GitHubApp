package com.hadimas.githubapp.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadimas.githubapp.api.ApiConfig
import com.hadimas.githubapp.response.UserItems
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {
     val listFollowing = MutableLiveData<ArrayList<UserItems>>()

    fun setListFollowing(username: String){
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<ArrayList<UserItems>>{
            override fun onResponse(
                call: Call<ArrayList<UserItems>>,
                response: Response<ArrayList<UserItems>>
            ) {
                if (response.isSuccessful){
                    listFollowing.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<UserItems>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getListFollowing() : LiveData<ArrayList<UserItems>>{
        return listFollowing
    }

}