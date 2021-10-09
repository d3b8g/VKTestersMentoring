package net.d3b8g.vktestersmentoring.back

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import net.d3b8g.vktestersmentoring.helper.Components.mMicro
import java.text.SimpleDateFormat
import java.util.*

class DictoCors : Service() {

    @SuppressLint("SimpleDateFormat")
    override fun onStart(intent: Intent?, startId: Int) {
        mMicro = MediaRecorder()
        mMicro?.let {
            it.setAudioSource(MediaRecorder.AudioSource.MIC)
            it.setAudioSamplingRate(44100)
            it.setAudioEncodingBitRate(96000)
            it.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            it.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            it.setOutputFile(
                this.getExternalFilesDir(null)?.absolutePath +
                        SimpleDateFormat("yyyy_mm_dd_hh:mm:ss").format(Date()))
            it.prepare()
            it.start()
        }
    }

    override fun onBind(intent: Intent?): IBinder? =  null

}