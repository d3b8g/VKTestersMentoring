package net.d3b8g.vktestersmentoring.helper

import android.media.MediaPlayer
import android.media.MediaRecorder

class Components {
    companion object{
        val mediaTypeGallery = listOf("Аудиозаписи","test")
        var mMicro:MediaRecorder? = null
        var mMicroActive = false
        var mPlayer:MediaPlayer? = null
    }
}