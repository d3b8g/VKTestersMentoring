package net.d3b8g.vktestersmentoring.ui.dicto

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.back.DictoCors
import net.d3b8g.vktestersmentoring.databinding.FragmentDictoBinding
import net.d3b8g.vktestersmentoring.helper.Components.mMicro
import net.d3b8g.vktestersmentoring.helper.Components.mMicroActive
import net.d3b8g.vktestersmentoring.ui.customUI.FragmentHeader
import net.d3b8g.vktestersmentoring.ui.dicto.DictaphonePermission.haveDictaphonesPermission

class Dictaphone : Fragment(R.layout.fragment_dicto) {

    private lateinit var binding: FragmentDictoBinding
    private val fragmentHeader: FragmentHeader by lazy {
        binding.bugsHeader
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDictoBinding.bind(view)

        fragmentHeader.setTitleText("Диктофон")
        fragmentHeader.setRightButtonIcon(
            ResourcesCompat.getDrawable(resources ,R.drawable.ic_close, resources.newTheme())!!
        )
        fragmentHeader.setRightButtonListener {
            findNavController().popBackStack()
        }

        binding.btnRecording.setOnClickListener {
            if (mMicroActive) {
                mMicro?.stop()
                mMicro?.release()
                mMicro = null
                binding.btnRecording.setBackgroundDrawable(requireContext().getDrawable(R.drawable.ic_mic))
                mMicroActive = false
                net.d3b8g.vktestersmentoring.ui.longread.MediaCenter.recording_anim = false
            } else {
                if (!requireContext().haveDictaphonesPermission()) {
                    if (Build.VERSION.SDK_INT > 28) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO
                            ), 502
                        )
                    } else {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO
                            ), 502
                        )
                    }
                } else {
                    requireContext().startService(Intent(requireActivity(), DictoCors::class.java))
                    binding.btnRecording.setBackgroundDrawable(requireContext().getDrawable(R.drawable.ic_stop))
                    (context as MediaCenterInterface).startRecordingComponents()
                    mMicroActive = true
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            502 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requireContext().startService(Intent(requireActivity(), DictoCors::class.java))
                    binding.btnRecording.setBackgroundDrawable(requireContext().getDrawable(R.drawable.ic_stop))
                    (requireActivity() as MediaCenterInterface).startRecordingComponents()
                } else {
                    Toast.makeText(requireContext(),"Прокинь права приложению для микрофона", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}