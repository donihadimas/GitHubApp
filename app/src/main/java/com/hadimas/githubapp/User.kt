package com.hadimas.githubapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var username : String,
    var name : String,
    var company : String,
//    var location : String,
//    var repo : Int,
//    var follower : Int,
//    var following : Int,
    var avatar : Int
): Parcelable