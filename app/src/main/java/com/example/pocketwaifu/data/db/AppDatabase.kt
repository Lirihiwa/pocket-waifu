package com.example.pocketwaifu.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pocketwaifu.data.models.AvatarEntity
import com.example.pocketwaifu.data.models.ChatEntity

@Database(
    entities = [
        AvatarEntity::class,
        ChatEntity::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val avatarDao: AvatarDAO
    abstract val chatDao: ChatDAO
}