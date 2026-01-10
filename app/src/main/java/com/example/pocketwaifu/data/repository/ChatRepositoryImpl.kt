package com.example.pocketwaifu.data.repository

import android.util.Log
import com.example.pocketwaifu.data.db.ChatDAO
import com.example.pocketwaifu.data.models.ChatEntity
import com.example.pocketwaifu.data.network.AIApiService
import com.example.pocketwaifu.data.network.AiParsedResponse
import com.example.pocketwaifu.data.network.ChatMessage
import com.example.pocketwaifu.data.network.ChatRequest
import com.example.pocketwaifu.data.models.Emotions
import com.example.pocketwaifu.data.models.SendMessageResult
import com.example.pocketwaifu.data.network.PromptService
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

class ChatRepositoryImpl(
    val service: AIApiService,
    private val promptService: PromptService,
    val dao: ChatDAO,
    private val json: Json
) : ChatRepository {

    private val apiKey = "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"

    override suspend fun sendMessage(message: ChatEntity, promptText: String) : SendMessageResult {
        dao.insertMessage(message)

        val history = dao.getLastMessages(message.avatarId, 15).reversed()

        val apiMessages = mutableListOf<ChatMessage>()
        apiMessages.add(
            ChatMessage(
                role = "system",
                content = promptService.getSystemPrompt(promptText)
            )
        )

        history.forEach { entity ->
            apiMessages.add(
                ChatMessage(
                    role = if (entity.isUser) "user" else "assistant",
                    content = entity.text ?: ""
                )
            )
        }

        val response = service.getChatCompletion("Bearer $apiKey", ChatRequest(messages = apiMessages))

        val rawJsonFromAi = response.choices.first().message.content
        Log.d("CHECK_DATA", "Raw content from AI: $rawJsonFromAi")
        val parsed = try {
            json.decodeFromString<AiParsedResponse>(rawJsonFromAi)
        } catch (e: Exception) {
            Log.e("CHECK_DATA", "Парсинг провалился: ${e.message}")
            AiParsedResponse(text = rawJsonFromAi, emotion = "neutral")
        }

        val aiMessage = ChatEntity(
            avatarId = message.avatarId,
            text = parsed.text,
            isUser = false
        )

        dao.insertMessage(aiMessage)

        return SendMessageResult(
            message = aiMessage,
            emotion = Emotions.fromString(parsed.emotion)
        )
    }

    override fun getAllMessagesForAvatar(avatarId: Long): Flow<List<ChatEntity>> {
        return dao.getAllMessagesForAvatar(avatarId)
    }
}