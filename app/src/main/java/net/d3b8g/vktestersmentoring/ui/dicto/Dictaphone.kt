package net.d3b8g.vktestersmentoring.ui.dicto

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.back.DictoCors
import net.d3b8g.vktestersmentoring.helper.Components.Companion.mMicro
import net.d3b8g.vktestersmentoring.helper.Components.Companion.mMicroActive
import net.d3b8g.vktestersmentoring.interfaces.MediaCenter

class Dictaphone : Fragment(R.layout.fragment_dicto) {

    lateinit var btnRecord: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRecord = view.findViewById(R.id.btn_recording)
        btnRecord.setOnClickListener {
            if(mMicroActive) {
                mMicro?.stop()
                mMicro?.release()
                mMicro = null
                btnRecord.setBackgroundDrawable(requireContext().getDrawable(R.drawable.ic_mic))
                mMicroActive = false
                net.d3b8g.vktestersmentoring.ui.home.MediaCenter.recording_anim = false
            } else {
                if(!checkAudioPerm()) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO
                        ), 502)
                } else {
                    requireActivity().startService(Intent(requireContext(), DictoCors::class.java))
                    btnRecord.setBackgroundDrawable(requireContext().getDrawable(R.drawable.ic_stop))
                    (context as MediaCenter).startRecordingComponents()
                    mMicroActive = true
                }
            }
        }
    }

    private fun checkAudioPerm(): Boolean {
        val write_external_storage_result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read_external_storage_result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        val record_audio_result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED &&
                read_external_storage_result == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            502 -> {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    requireActivity().startService(Intent(requireContext(), DictoCors::class.java))
                    btnRecord.setBackgroundDrawable(requireContext().getDrawable(R.drawable.ic_stop))
                    (requireActivity() as MediaCenter).startRecordingComponents()
                } else {
                    Toast.makeText(requireContext(),"Прокинь права приложению для микрофона", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}