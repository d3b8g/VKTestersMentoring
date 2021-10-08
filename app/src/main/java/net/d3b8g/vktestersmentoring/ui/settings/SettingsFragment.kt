package net.d3b8g.vktestersmentoring.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.databinding.FragmentSettingsBinding
import net.d3b8g.vktestersmentoring.ui.login.LoginFragmentDirections

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

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

        binding.close.setOnClickListener {
            1.changeFragment()
        }

        logout.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
                putBoolean("make_splash", false)
            }
            0.changeFragment()
        }
    }

    fun Int.changeFragment() {
        val action = when (this) {
            0 -> SettingsFragmentDirections.actionNavSettingsToNavLogin()
            else -> SettingsFragmentDirections.actionNavSettingsToNavHome()
        }
        findNavController().navigate(action)
        (requireActivity() as net.d3b8g.vktestersmentoring.interfaces.ActionBar).actionBarChange(true)
    }
}