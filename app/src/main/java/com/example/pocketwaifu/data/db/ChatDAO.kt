package com.example.pocketwaifu.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pocketwaifu.data.models.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDAO {

    @Insert
    suspend fun insertMessage(message: ChatEntity)

    @Query("SELECT * FROM ${ChatEntity.TABLE} WHERE avatar_name = :avatarId ORDER BY creation_time DESC")
    fun getAllMessagesForAvatar(avatarId: Long): Flow<List<ChatEntity>>

    @Query("SELECT * FROM ${ChatEntity.TABLE} WHERE avatar_name = :avatarId ORDER BY creation_time DESC LIMIT :limit")
    suspend fun getLastMessages(avatarId: Long, limit: Int): List<ChatEntity>
}