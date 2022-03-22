package com.hadimas.githubapp.response

import com.google.gson.annotations.SerializedName

data class UsersSearchResponse(
	@field:SerializedName("items")
	val items: ArrayList<UserItems>
)

data class UserItems(
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("id")
	val id: Int,
)
