package com.example.pocketwaifu.presenter.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale

interface SpeechToTextManager {
    fun startListening(onResult: (String) -> Unit, onError: (String) -> Unit)
    fun stopListening()
}

class SpeechToTextManagerImpl(private val context: Context) : SpeechToTextManager {
    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    private val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    }

    override fun startListening(
        onResult: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        speechRecognizer.setRecognitionListener(
            object : RecognitionListener
            {
                override fun onResults(results: Bundle?) {
                    val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    onResult(data?.get(0) ?: "")
                }

                override fun onError(error: Int) {
                    val message = when (error) {
                        //ну тут можно больше обработать но мне лень
                        SpeechRecognizer.ERROR_NO_MATCH -> "Не удалось распознать речь"
                        else -> "Ошибка микрофона: $error"
                    }
                    onError(message)
                }

                override fun onBeginningOfSpeech() {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onRmsChanged(rmsdB: Float) {}
            }
        )

        speechRecognizer.startListening(intent)
    }

    override fun stopListening() {
        speechRecognizer.stopListening()
    }
}