package com.hadimas.githubapp.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadimas.githubapp.api.ApiConfig
import com.hadimas.githubapp.response.DetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel: ViewModel() {
    val detuser = MutableLiveData<DetailUserResponse>()

    fun setUserDetail(username : String){
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful){
                     detuser.postValue(response.body() )
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserDetail(): LiveData<DetailUserResponse>{
        return detuser
    }
}