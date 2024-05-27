package com.example.chatcomposeapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatcomposeapp.data.local.model.LocalUser

@Database(entities = [LocalUser::class], version = 1)
abstract class ChatDatabase() : RoomDatabase() {
    abstract val userDao: UserDao
}