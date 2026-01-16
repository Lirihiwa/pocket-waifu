package com.example.pocketwaifu.presenter.overlay

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketwaifu.data.models.AvatarEntity
import com.example.pocketwaifu.data.models.ChatEntity
import com.example.pocketwaifu.data.models.Emotions
import com.example.pocketwaifu.domain.GetAllMessagesForAvatarUseCase
import com.example.pocketwaifu.domain.GetAvatarByIdUseCase
import com.example.pocketwaifu.domain.SendMessageUseCase
import com.example.pocketwaifu.presenter.chat.SpeechToTextManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OverlayViewModel(
    private val avatarId: Int,
    private val getAllMessagesForAvatarUseCase: GetAllMessagesForAvatarUseCase,
    private val getAvatarByIdUseCase: GetAvatarByIdUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val speechToTextManager: SpeechToTextManager,
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatEntity>>(emptyList())
    val messages: StateFlow<List<ChatEntity>>
        get() = _messages.asStateFlow()

    private val _emotion = MutableStateFlow<Emotions>(Emotions.NEUTRAL)
    val emotion: StateFlow<Emotions>
        get() = _emotion.asStateFlow()

    private val _avatar = MutableStateFlow<AvatarEntity>(AvatarEntity(
        description = "",
    ))
    val avatar: StateFlow<AvatarEntity>
        get() = _avatar.asStateFlow()

    private val _isVoiceRecording = MutableStateFlow(false)
    val isVoiceRecording: StateFlow<Boolean> = _isVoiceRecording.asStateFlow()

    private val _isResponding = MutableStateFlow(false)


    private val _avatarScale = MutableStateFlow(1f)
    val avatarScale = _avatarScale.asStateFlow()

    private val _chatScale = MutableStateFlow(1f)
    val chatScale = _chatScale.asStateFlow()

    private val _transparency = MutableStateFlow(1f)
    val transparency = _transparency.asStateFlow()

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

    fun sendMessage(
        text: String,
    ) {

        viewModelScope.launch {

            try {
                val result = sendMessageUseCase(
                    message = ChatEntity(
                        avatarId = avatar.value.id,
                        text = text,
                        isUser = true,
                    ),
                    promptText = avatar.value.prompt
                )

                _emotion.value = result.emotion
            } catch (e: Exception) {
                Log.e("SEND_MESSAGE_EXCEPTION" ,e.toString())
            }
        }
    }

    fun setAvatarScale(scale: Float) {

        _avatarScale.value = scale
    }

    fun setChatScale(scale: Float) {

        _chatScale.value = scale
    }

    fun setTransparency(scale: Float) {

        _transparency.value = scale
    }

    fun toggleMicrophone() {
        if (_isVoiceRecording.value) {
            stopVoice()
        } else {
            startVoice()
        }
    }

    private fun startVoice() {

        _isVoiceRecording.value = true

        viewModelScope.launch(Dispatchers.Main) {
            speechToTextManager.startListening(
                onResult = { text ->
                    if (text.isNotBlank()) {
                        sendMessage(text)
                    }
                    _isVoiceRecording.value = false
                },
                onError = { errorMessage ->
                    Log.e("SpeechError", errorMessage)
                    _isVoiceRecording.value = false
                }
            )
        }
    }

    private fun stopVoice() {

        viewModelScope.launch(Dispatchers.Main) {
            speechToTextManager.stopListening()
        }
    }
}