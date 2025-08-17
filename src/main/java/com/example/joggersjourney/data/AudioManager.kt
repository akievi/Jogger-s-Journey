package com.example.joggersjourney.data

import android.content.Context
import android.media.ToneGenerator
import android.media.AudioManager as SystemAudioManager

class AudioManager(private val context: Context) {
    private var toneGenerator: ToneGenerator? = null
    private var isSoundEnabled = true
    
    init {
        initializeToneGenerator()
    }
    
    private fun initializeToneGenerator() {
        try {
            toneGenerator = ToneGenerator(
                SystemAudioManager.STREAM_NOTIFICATION,
                100
            )
        } catch (e: Exception) {
            // Fallback if ToneGenerator fails
            toneGenerator = null
        }
    }
    
    fun playChallengeStart() {
        if (isSoundEnabled) {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
        }
    }
    
    fun playChallengeSuccess() {
        if (isSoundEnabled) {
            // High pitched success sound
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 300)
        }
    }
    
    fun playChallengeFail() {
        if (isSoundEnabled) {
            // Low pitched failure sound
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 400)
        }
    }
    
    fun playSpeedWarning() {
        if (isSoundEnabled) {
            // Short warning beep
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 150)
        }
    }
    
    fun playSpeedGood() {
        if (isSoundEnabled) {
            // Positive confirmation sound
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
        }
    }
    
    fun setSoundEnabled(enabled: Boolean) {
        isSoundEnabled = enabled
    }
    
    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
} 