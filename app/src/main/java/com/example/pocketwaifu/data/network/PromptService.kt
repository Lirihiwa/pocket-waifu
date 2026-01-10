package com.example.pocketwaifu.data.network

interface PromptService{
    fun getSystemPrompt(basePrompt: String): String
}
class PromptServiceImpl : PromptService {
    override fun getSystemPrompt(basePrompt: String): String {
        return """
            $basePrompt
            
            ВАЖНО: Отвечай ТОЛЬКО в формате JSON.
            Формат ответа: 
            {
              "text": "текст твоего ответа",
              "emotion": "название эмоции (joy, anger, love, fear, neutral)"
            }
        """.trimIndent()
    }
}