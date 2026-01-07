package com.example.pocketwaifu.data.repository

import com.example.pocketwaifu.data.models.AvatarEntity
import kotlinx.coroutines.flow.Flow

interface AvatarRepository {
    fun getAllAvatars(): Flow<List<AvatarEntity>>
    suspend fun initializeDatabase()
}