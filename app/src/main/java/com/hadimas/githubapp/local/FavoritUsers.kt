package com.hadimas.githubapp.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "fav_users")
data class FavoritUsers(
    val login: String,

    @PrimaryKey
    val id: Int,

    val avatar_url : String

): Serializable