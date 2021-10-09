package net.d3b8g.vktestersmentoring.helper

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.media.MediaPlayer
import android.media.MediaRecorder

object Components {
    val mediaTypeGallery = listOf("Аудиозаписи","test")
    var mMicro: MediaRecorder? = null
    var mMicroActive = false
    var mPlayer: MediaPlayer? = null
}