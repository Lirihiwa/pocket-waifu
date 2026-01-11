package com.example.pocketwaifu.domain

import com.example.pocketwaifu.data.models.AvatarEntity
import com.example.pocketwaifu.data.models.ChatEntity
import com.example.pocketwaifu.data.repository.AvatarRepository
import com.example.pocketwaifu.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

interface GetAvatarByIdUseCase {
    suspend operator fun invoke(avatarId: Int) : AvatarEntity
}

class GetAvatarByIdUseCaseImpl(val repository: AvatarRepository) : GetAvatarByIdUseCase {
    override suspend fun invoke(avatarId: Int) : AvatarEntity =
        repository.getAvatarById(avatarId)
}