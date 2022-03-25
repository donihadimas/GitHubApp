package com.hadimas.githubapp.api

import com.hadimas.githubapp.BuildConfig
import com.hadimas.githubapp.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object {
        const val token: String = BuildConfig.GITHUB_TOKEN
    }

    @GET("search/users")
    @Headers("Authorization: token $token")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UsersSearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $token ")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $token")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<UserItems>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $token")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<UserItems>>
}