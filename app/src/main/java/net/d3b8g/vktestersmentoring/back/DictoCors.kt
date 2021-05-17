package net.d3b8g.vktestersmentoring.back

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import net.d3b8g.vktestersmentoring.helper.Components.Companion.mMicro
import net.d3b8g.vktestersmentoring.helper.PathHelper.Companion.audioPath
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DictoCors:Service() {

    override fun onStart(intent: Intent?, startId: Int) {
        mMicro = MediaRecorder()

        val path_mic = File(audioPath)

        if (!path_mic.exists() || !path_mic.isDirectory) path_mic.mkdirs()

        mMicro?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            try{
                setAudioSamplingRate(44100)
                setAudioEncodingBitRate(96000)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioPath + SimpleDateFormat("yyyy_mm_dd_hh:mm:ss").format(Date()))
                prepare()
                start()
            }catch (e:Exception){
                Log.e("VKTM_SYS", "Access_error $e")
            }
        }
//        NotificationCompagain(this).createNotificationChannel(
//            ConstantsApp.channelIdAudio,
//            ConstantsApp.channelName,
//            getString(R.string.clickhere_to_stop),1)
    }

    override fun onBind(intent: Intent?): IBinder? =  null

}