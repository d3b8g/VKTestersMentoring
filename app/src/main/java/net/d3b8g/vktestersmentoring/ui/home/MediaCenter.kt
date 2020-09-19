package net.d3b8g.vktestersmentoring.ui.home

import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.helper.PathHelper.Companion.audioPath
import java.io.File
import java.util.*

class MediaCenter:Fragment() {

    lateinit var b1: RelativeLayout
    lateinit var b2: ImageView
    lateinit var btn_rec: ImageButton
    lateinit var status_t: TextView

    companion object{
        var recording_anim = false
        var mDicto: MediaRecorder? = null
        var isDicto:Boolean = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.fragment_mcenter,container,false)

        btn_rec = inflate.findViewById(R.id.btn_action_player)
        b1 = inflate.findViewById(R.id.recording_backg)
        b2 = inflate.findViewById(R.id.recording_stat)
        status_t = inflate.findViewById(R.id.status_recording)

        btn_rec.setOnClickListener {
            recording_anim = !recording_anim
            if(recording_anim){
                recordingMic()
                mediaCenterAnimateRecording()
                btn_rec.setImageDrawable(resources.getDrawable(R.drawable.ic_stop))
                status_t.text = "Записываем аудио"
            }
            else {
                mDicto?.stop()
                mDicto?.release()
                if(isDicto) btn_rec.setImageDrawable(resources.getDrawable(R.drawable.ic_mic))
                else btn_rec.setImageDrawable(resources.getDrawable(R.drawable.ic_start))
                status_t.text = "Запись остановлена"
            }
        }

        mediaCenterAnimateRecording()

        if(recording_anim){
            recordingMic()
            btn_rec.setImageDrawable(resources.getDrawable(R.drawable.ic_stop))
            status_t.text = "Записываем аудио"
        }
        else {
            mDicto?.let {
                it?.stop()
                it?.release()
                if(isDicto) btn_rec.setImageDrawable(resources.getDrawable(R.drawable.ic_mic))
                else btn_rec.setImageDrawable(resources.getDrawable(R.drawable.ic_start))
                status_t.text = "Запись остановлена"
            }
        }

        return inflate
    }

    fun recordingMic(){
        mDicto = MediaRecorder()

        val path_mic = File("${Environment.getExternalStorageDirectory().absolutePath}/audio/")
        File(audioPath).mkdirs()

        var file_out: File = File.createTempFile("${DateFormat.format("MM-dd_kk-mm", Date().time)}_audio",".3gp",path_mic)

        mDicto.let {
            it!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            try{
                it.setAudioSamplingRate(44100)
                it.setAudioEncodingBitRate(96000)
                it.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                it.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                it.setOutputFile(file_out.absolutePath)
                it.prepare()
                it.start()
            }catch (e:Exception){
                Toast.makeText(requireContext(),"Без доступа к микрофону не могу начать запись", Toast.LENGTH_SHORT)
            }
        }
    }

    fun mediaCenterAnimateRecording(){
        Thread{
            activity?.runOnUiThread {
                if(recording_anim){
                    b1.visibility = View.VISIBLE
                    b2.animate().alpha(1.0f).duration = 600
                    Handler().postDelayed({
                        b2.animate().alpha(0.0f).duration = 400
                    },900)
                    Handler().postDelayed({
                        mediaCenterAnimateRecording()
                    },1300)
                }else {
                    b1.visibility = View.GONE
                }
            }
        }.start()
    }

}