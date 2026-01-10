package com.example.pocketwaifu.data.network

import com.example.pocketwaifu.data.models.ChatEntity
import com.example.pocketwaifu.data.models.Emotions
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AIApiService {
    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") apiKey: String,
        @Body request: ChatRequest
    ): ChatResponse
}

data class ChatRequest(
    val model: String = "llama-3.1-8b-instant",
    val messages: List<ChatMessage>,
    @SerializedName("response_format")
    val responseFormat: ResponseFormat = ResponseFormat()
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ResponseFormat(
    val type: String = "json_object"
)

data class ChatResponse(
    val choices: List<Choice>
) {
    data class Choice(val message: ChatMessage)
}

@kotlinx.serialization.Serializable
data class AiParsedResponse(
    val text: String,
    val emotion: String
)