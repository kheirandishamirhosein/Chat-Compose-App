package com.example.chatcomposeapp.data.local.db

import androidx.room.Dao
import androidx.room.Upsert
import com.example.chatcomposeapp.data.local.model.LocalUser

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUser(user: LocalUser)

}