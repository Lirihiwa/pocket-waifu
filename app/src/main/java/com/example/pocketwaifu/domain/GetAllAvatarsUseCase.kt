package com.example.pocketwaifu.domain

import com.example.pocketwaifu.data.models.AvatarEntity
import com.example.pocketwaifu.data.repository.AvatarRepository
import kotlinx.coroutines.flow.Flow

interface GetAllAvatarsUseCase {
    operator fun invoke() : Flow<List<AvatarEntity>>
}


class GetAllAvatarsUseCaseImpl(val repository: AvatarRepository) : GetAllAvatarsUseCase {

    override fun invoke() : Flow<List<AvatarEntity>> =
        repository.getAllAvatars()
}