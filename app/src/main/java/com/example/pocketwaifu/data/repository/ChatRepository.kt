package com.example.pocketwaifu.data.repository

import com.example.pocketwaifu.data.models.ChatEntity
import com.example.pocketwaifu.data.models.SendMessageResult
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(message: ChatEntity, promptText: String) : SendMessageResult
    fun getAllMessagesForAvatar(avatarId: Int) : Flow<List<ChatEntity>>
}