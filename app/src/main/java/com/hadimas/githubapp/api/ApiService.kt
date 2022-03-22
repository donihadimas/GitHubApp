package com.hadimas.githubapp.api

import com.hadimas.githubapp.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UsersSearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username : String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ):Call<ArrayList<UserItems>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ):Call<ArrayList<UserItems>>
}