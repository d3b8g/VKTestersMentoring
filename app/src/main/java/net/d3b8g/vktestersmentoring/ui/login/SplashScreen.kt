package net.d3b8g.vktestersmentoring.ui.login

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.d3b8g.vktestersmentoring.R

class SplashScreen : Fragment(R.layout.splash_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            delay(1500L)
            PreferenceManager.getDefaultSharedPreferences(requireContext()).apply {
                val action = if (getInt("active_user_id", -1) > 0) SplashScreenDirections.actionNavSplashToNavHome()
                else SplashScreenDirections.actionNavSplashToNavLogin()

                val navOption = NavOptions.Builder().setPopUpTo(R.id.nav_home, true).build()

                findNavController().navigate(action, navOption)
            }
        }
    }
}