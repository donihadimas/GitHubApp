package com.hadimas.githubapp.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavUserDao {
    @Insert
    fun addFavorit(favUser: FavoritUsers)

    @Query("select * from fav_users")
    fun getFavUsers(): LiveData<List<FavoritUsers>>

    @Query("select count(*) from fav_users where fav_users.id = :id")
    fun cekUser(id: Int): Int

    @Query("delete from fav_users where fav_users.id = :id" )
    fun delUserFav(id: Int): Int
}