package com.hadimas.githubapp.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.hadimas.githubapp.SettingPreferences
import com.hadimas.githubapp.api.ApiConfig
import com.hadimas.githubapp.response.UserItems
import com.hadimas.githubapp.response.UsersSearchResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel (private val pref: SettingPreferences) : ViewModel() {
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

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

}