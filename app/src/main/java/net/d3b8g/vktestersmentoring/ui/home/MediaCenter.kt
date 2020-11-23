package net.d3b8g.vktestersmentoring.ui.home

import android.content.Intent
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.back.DictoCors
import net.d3b8g.vktestersmentoring.helper.Components.Companion.mMicroActive
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
                requireActivity().startService(Intent(requireContext(),DictoCors::class.java))
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
            btn_rec.setImageDrawable(resources.getDrawable(R.drawable.ic_stop))
            mediaCenterAnimateRecording()
            status_t.text = "Записываем аудио"
        }
        else {
            mDicto?.stop()
            mDicto?.release()
            if(isDicto) btn_rec.setImageDrawable(resources.getDrawable(R.drawable.ic_mic))
            else btn_rec.setImageDrawable(resources.getDrawable(R.drawable.ic_start))
            status_t.text = "Запись остановлена"
        }

        return inflate
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
                    mDicto?.stop()
                    mDicto?.release()
                    if(isDicto) btn_rec.setImageDrawable(resources.getDrawable(R.drawable.ic_mic))
                    else btn_rec.setImageDrawable(resources.getDrawable(R.drawable.ic_start))
                    status_t.text = "Запись остановлена"
                }
            }
        }.start()
    }

}