package net.d3b8g.vktestersmentoring.ui.longread

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.content.Intent
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.back.DictoCors
import net.d3b8g.vktestersmentoring.databinding.FragmentMcenterBinding
import net.d3b8g.vktestersmentoring.helper.Components.mMicroActive

class MediaCenter: Fragment(R.layout.fragment_mcenter) {
    private lateinit var binding: FragmentMcenterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMcenterBinding.bind(view)

        binding.btnActionPlayer.setOnClickListener {
            recording_anim = !recording_anim
            if(recording_anim) {
                requireContext().startService(Intent(requireActivity(), DictoCors::class.java))
                mediaCenterAnimateRecording()
                binding.btnActionPlayer.setImageDrawable(resources.getDrawable(R.drawable.ic_stop))
                binding.statusRecording.text = "Записываем аудио"
            } else {
                mDicto?.stop()
                mDicto?.release()
                if(isDicto) binding.btnActionPlayer.setImageDrawable(resources.getDrawable(R.drawable.ic_mic))
                else binding.btnActionPlayer.setImageDrawable(resources.getDrawable(R.drawable.ic_start))
                binding.statusRecording.text = "Запись остановлена"
                mMicroActive = false
            }
        }

        mediaCenterAnimateRecording()

        if(recording_anim) {
            binding.btnActionPlayer.setImageDrawable(resources.getDrawable(R.drawable.ic_stop))
            mediaCenterAnimateRecording()
            binding.statusRecording.text = "Записываем аудио"
        } else {
            mDicto?.stop()
            mDicto?.release()
            if(isDicto) binding.btnActionPlayer.setImageDrawable(resources.getDrawable(R.drawable.ic_mic))
            else binding.btnActionPlayer.setImageDrawable(resources.getDrawable(R.drawable.ic_start))
            binding.statusRecording.text = "Запись остановлена"
        }
    }

    private fun mediaCenterAnimateRecording() {
        Thread {
            activity?.runOnUiThread {
                if(recording_anim) {
                    binding.recordingBackg.visibility = View.VISIBLE
                    binding.recordingStat.animate().alpha(1.0f).duration = 600
                    Handler().postDelayed({
                        binding.recordingStat.animate().alpha(0.0f).duration = 400
                    },900)
                    Handler().postDelayed({
                        mediaCenterAnimateRecording()
                    },1300)
                } else {
                    binding.recordingBackg.visibility = View.GONE
                    mDicto?.stop()
                    mDicto?.release()
                    if(isDicto) binding.btnActionPlayer.setImageDrawable(resources.getDrawable(R.drawable.ic_mic))
                    else binding.btnActionPlayer.setImageDrawable(resources.getDrawable(R.drawable.ic_start))
                    binding.statusRecording.text = "Запись остановлена"
                }
            }
        }.start()
    }

    companion object {
        var recording_anim = mMicroActive
        var mDicto: MediaRecorder? = null
        var isDicto: Boolean = true
    }
}