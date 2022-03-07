package com.hadimas.githubapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String,
    var name: String,
    var repo: String,
    var avatar: Int,
    var location: String,
    var company: String,
    var follower: String,
    var following: String,
) : Parcelable