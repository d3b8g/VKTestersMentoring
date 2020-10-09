package net.d3b8g.vktestersmentoring.back

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.text.format.DateFormat
import android.widget.Toast
import net.d3b8g.vktestersmentoring.helper.Components.Companion.mMicro
import net.d3b8g.vktestersmentoring.helper.PathHelper.Companion.audioPath
import java.io.File
import java.util.*

class DictoCors:Service() {

    override fun onStart(intent: Intent?, startId: Int) {
        mMicro = MediaRecorder()

        val path_mic = File(audioPath)

        var file_out: File = File.createTempFile("${DateFormat.format("MM-dd_kk-mm", Date().time)}_audio",".3gp",path_mic)

        mMicro?.let {
            it.setAudioSource(MediaRecorder.AudioSource.MIC)
            try{
                it.setAudioSamplingRate(44100)
                it.setAudioEncodingBitRate(96000)
                it.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                it.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                it.setOutputFile(file_out.absolutePath)
                it.prepare()
                it.start()
            }catch (e:Exception){
                Toast.makeText(this,"Дай доступ микрофону.",Toast.LENGTH_SHORT).show()
            }
        }
//        NotificationCompagain(this).createNotificationChannel(
//            ConstantsApp.channelIdAudio,
//            ConstantsApp.channelName,
//            getString(R.string.clickhere_to_stop),1)
    }

    override fun onBind(intent: Intent?): IBinder? =  null

}