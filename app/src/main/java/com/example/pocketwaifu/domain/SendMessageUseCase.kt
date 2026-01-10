package com.example.pocketwaifu.domain

import com.example.pocketwaifu.data.models.ChatEntity
import com.example.pocketwaifu.data.models.SendMessageResult
import com.example.pocketwaifu.data.repository.ChatRepository

interface SendMessageUseCase {
    suspend operator fun invoke(message: ChatEntity, promptText: String) : SendMessageResult
}

class SendMessageUseCaseImpl(val repository: ChatRepository) : SendMessageUseCase {
    override suspend fun invoke(message: ChatEntity, promptText: String) : SendMessageResult =
        repository.sendMessage(message, promptText)
}