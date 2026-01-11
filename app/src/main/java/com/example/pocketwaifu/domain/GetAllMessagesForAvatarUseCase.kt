package com.example.pocketwaifu.domain

import com.example.pocketwaifu.data.models.ChatEntity
import com.example.pocketwaifu.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

interface GetAllMessagesForAvatarUseCase {
    operator fun invoke(avatarId: Int) : Flow<List<ChatEntity>>
}


class GetAllMessagesForAvatarUseCaseImpl(val repository: ChatRepository) : GetAllMessagesForAvatarUseCase {
    override fun invoke(avatarId: Int) : Flow<List<ChatEntity>> =
        repository.getAllMessagesForAvatar(avatarId)
}