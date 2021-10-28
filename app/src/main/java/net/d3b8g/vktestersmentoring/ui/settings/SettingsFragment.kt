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
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.databinding.FragmentSettingsBinding
import net.d3b8g.vktestersmentoring.ui.customUI.FragmentHeader

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
                .setPositiveButton("Выйти") {_, _ ->
                    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
                        putInt("active_user_id", -1)
                    }
                    0.changeFragment()
                }
                .setNegativeButton("Отменить") {d, _ ->
                    d.dismiss()
                }
                .show()
        }

        fragmentHeader.setTitleText("Настройки")
        fragmentHeader.setRightButtonIcon(
            ResourcesCompat.getDrawable(resources ,R.drawable.ic_close, resources.newTheme())!!
        )
        fragmentHeader.setRightButtonListener {
            findNavController().popBackStack()
        }
    }

    private fun Int.changeFragment() {
        val action = when (this) {
            0 -> SettingsFragmentDirections.actionNavSettingsToNavLogin()
            else -> SettingsFragmentDirections.actionNavSettingsToNavLogin()
        }
        findNavController().navigate(action)
    }
}