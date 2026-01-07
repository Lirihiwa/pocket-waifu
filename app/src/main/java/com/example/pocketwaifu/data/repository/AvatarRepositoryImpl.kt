package com.example.pocketwaifu.data.repository

import com.example.pocketwaifu.data.db.AvatarDAO
import com.example.pocketwaifu.data.models.AvatarEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

class AvatarRepositoryImpl(
    val dao: AvatarDAO,
    val context: android.content.Context
) : AvatarRepository {

    override fun getAllAvatars(): Flow<List<AvatarEntity>> {
        return dao.getAllAvatars()
    }

    override suspend fun initializeDatabase() {
        val avatarsCount = dao.getAvatarsCount()

        if (avatarsCount == 0) {
            val jsonString = context.assets.open("data/avatars_config.json")
                .bufferedReader()
                .use { it.readText() }

            val avatarList: List<AvatarEntity> = Json.decodeFromString<List<AvatarEntity>>(jsonString)

            dao.insertBaseAvatars(avatarList)
        }
    }
}