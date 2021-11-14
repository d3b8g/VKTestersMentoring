package net.d3b8g.vktestersmentoring.ui.settings

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import net.d3b8g.vktestersmentoring.MainActivity
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.customUI.fragmentHeader.FragmentHeader
import net.d3b8g.vktestersmentoring.databinding.FragmentSettingsBinding
import net.d3b8g.vktestersmentoring.helper.UITypes

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding
    private val fragmentHeader: FragmentHeader by lazy {
        binding.bugsHeader
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSettingsBinding.bind(view)

        val tracking = binding.sTracking
        val avatar = binding.sAvatar
        val logout = binding.logout


        PreferenceManager.getDefaultSharedPreferences(requireContext()).apply {
            tracking.isChecked = getBoolean("do_tracking", false)
            avatar.isChecked = getBoolean("do_avatar", false)
        }

        tracking.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
                putBoolean("do_tracking", tracking.isChecked)
            }
        }

        avatar.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
                putBoolean("do_avatar", avatar.isChecked)
            }
        }

        logout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Выти из аккаунта?")
                .setNegativeButton("Выйти") {_, _ ->
                    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
                        putInt("active_user_id", -1)
                    }
                    val action = SettingsFragmentDirections.actionNavSettingsToNavLogin()
                    findNavController().navigate(action)
                    (requireActivity() as MainActivity).run {
                        updateUI(UITypes.HIDE_TABBAR)
                    }
                }
                .setPositiveButton("Отменить") {d, _ ->
                    d.dismiss()
                }
                .show()
        }

        binding.hideTab.setOnClickListener {
            if (binding.hideTab.isChecked) {

            } else {

            }
        }

        fragmentHeader.apply {
            setTitleText("Настройки")
            setRightButtonIcon(
                ResourcesCompat.getDrawable(resources ,R.drawable.ic_close, resources.newTheme())!!
            ){
                findNavController().popBackStack()
            }
        }
    }
}