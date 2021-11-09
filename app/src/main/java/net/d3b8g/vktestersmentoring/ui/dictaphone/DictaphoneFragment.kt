package net.d3b8g.vktestersmentoring.ui.dictaphone

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.customUI.fragmentHeader.FragmentHeader
import net.d3b8g.vktestersmentoring.customUI.mediaCenter.FragmentMediaCenter
import net.d3b8g.vktestersmentoring.databinding.FragmentDictoBinding
import net.d3b8g.vktestersmentoring.interfaces.UpdateMainUI
import net.d3b8g.vktestersmentoring.ui.dictaphone.DictaphonePermission.haveDictaphonesPermission
import net.d3b8g.vktestersmentoring.ui.gallery.Gallery.audioPath
import java.text.SimpleDateFormat
import java.util.*

class DictaphoneFragment : Fragment(R.layout.fragment_dicto), DictaphoneInterface {

    private lateinit var binding: FragmentDictoBinding
    private val fragmentHeader: FragmentHeader by lazy {
        binding.bugsHeader
    }

    companion object {
        var microphoneState: Boolean = false
        var mMicro: MediaRecorder? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDictoBinding.bind(view)

        fragmentHeader.apply {
            setTitleText("Диктофон")
            setRightButtonIcon(
                ResourcesCompat.getDrawable(resources ,R.drawable.ic_close, resources.newTheme())!!
            ){
                findNavController().popBackStack()
            }
        }

        binding.btnRecording.setOnClickListener {
            if (!microphoneState) {
                // Ready to start recoding
                if (requireContext().haveDictaphonesPermission()) {
                    // All rights granted
                    startRecordingMicrophone()
                } else {
                    // Request for more rights
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        getRightsArray(),
                        502
                    )
                }
            } else {
                binding.btnRecording.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_mic)
                FragmentMediaCenter.recording_anim = false
                mMicro?.apply {
                    stop()
                    reset()
                    release()
                }
                mMicro = null
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            502 -> {
                grantResults.forEachIndexed { index, i ->
                    if (i == PackageManager.PERMISSION_GRANTED && index == grantResults.size - 1) {
                        startRecordingMicrophone()
                    } else {
                        Toast.makeText(requireContext(), "Не хватает прав", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun startRecordingMicrophone() {
        lifecycleScope.launch { recordingMicrophone() }
        binding.btnRecording.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_stop)
        (context as UpdateMainUI).launchMediaCenter()
        microphoneState = true
    }

    @SuppressLint("SimpleDateFormat")
    override suspend fun recordingMicrophone() = withContext(Dispatchers.IO) {
        try {
            mMicro = MediaRecorder()
            mMicro?.let {
                it.setAudioSource(MediaRecorder.AudioSource.MIC)
                it.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                it.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                it.setOutputFile(requireContext().audioPath(SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date())))
                it.prepare()
                it.start()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.stackTrace.toString() + e.message, Toast.LENGTH_LONG).show()
        }

    }

    private fun getRightsArray(): Array<String> {
        return when {
            Build.VERSION.SDK_INT > 28 -> {
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                )
            }
            else -> {
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                )
            }
        }
    }
}