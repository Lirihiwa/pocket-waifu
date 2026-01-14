package com.example.pocketwaifu.presenter.overlay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pocketwaifu.data.models.AvatarEntity
import com.example.pocketwaifu.data.models.ChatEntity
import com.example.pocketwaifu.data.repository.AvatarRepository
import com.example.pocketwaifu.domain.GetAllMessagesForAvatarUseCase
import com.example.pocketwaifu.domain.GetAvatarByIdUseCase
import com.example.pocketwaifu.domain.SendMessageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OverlayViewModel(
    private val avatarId: Int,
    val getAllMessagesForAvatarUseCase: GetAllMessagesForAvatarUseCase,
    val getAvatarByIdUseCase: GetAvatarByIdUseCase,
    val sendMessageUseCase: SendMessageUseCase,
    val repository: AvatarRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatEntity>>(emptyList())
    val messages: StateFlow<List<ChatEntity>>
        get() = _messages.asStateFlow()

    private val _avatar = MutableStateFlow<AvatarEntity?>(null)
    val avatar: StateFlow<AvatarEntity?>
        get() = _avatar.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {

            getAllMessagesForAvatarUseCase(avatarId).collect { messages ->
                _messages.value = messages
            }
        }

        viewModelScope.launch(Dispatchers.IO) {

            val loadedAvatar = getAvatarByIdUseCase(avatarId)
            _avatar.value = loadedAvatar
        }
    }
}