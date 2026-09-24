package com.example.audio

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private val tag = "TtsManager"
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false
    private var speechRate = 1.0f

    // Callback store for utterance completions
    private val completionCallbacks = mutableMapOf<String, () -> Unit>()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val hindiLocale = Locale("hi", "IN")
            val result = tts?.setLanguage(hindiLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to generic hindi or default locale
                val fallbackResult = tts?.setLanguage(Locale("hi"))
                if (fallbackResult == TextToSpeech.LANG_MISSING_DATA || fallbackResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w(tag, "Hindi TTS not fully supported on this device; using default locale")
                }
            }
            tts?.setSpeechRate(speechRate)
            tts?.setPitch(1.0f)
            setupListener()
            isInitialized = true
            Log.d(tag, "TTS successfully initialized")
        } else {
            Log.e(tag, "TTS Initialization failed with status: $status")
        }
    }

    private fun setupListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                // Speech started
            }

            override fun onDone(utteranceId: String?) {
                utteranceId?.let { id ->
                    completionCallbacks.remove(id)?.invoke()
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                utteranceId?.let { id ->
                    completionCallbacks.remove(id)?.invoke()
                }
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                utteranceId?.let { id ->
                    completionCallbacks.remove(id)?.invoke()
                }
            }
        })
    }

    fun setSpeechRate(rate: Float) {
        speechRate = rate
        if (isInitialized) {
            tts?.setSpeechRate(rate)
        }
    }

    fun speak(text: String, onComplete: (() -> Unit)? = null) {
        if (!isInitialized) {
            Log.w(tag, "TTS not ready yet for text: $text")
            onComplete?.invoke()
            return
        }

        val utteranceId = "utt_${System.currentTimeMillis()}_${text.hashCode()}"
        if (onComplete != null) {
            completionCallbacks[utteranceId] = onComplete
        }

        val params = Bundle()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    fun stop() {
        completionCallbacks.clear()
        tts?.stop()
    }

    fun shutdown() {
        completionCallbacks.clear()
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
