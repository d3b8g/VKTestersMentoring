package net.d3b8g.vktestersmentoring.ui.login


import android.app.ActionBar
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import net.d3b8g.vktestersmentoring.MainActivity
import net.d3b8g.vktestersmentoring.R

class SplashScreen : Fragment(R.layout.splash_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Handler().postDelayed({
            PreferenceManager.getDefaultSharedPreferences(requireContext()).apply {
                val action = if (getInt("active_user_id", -1) > 0) SplashScreenDirections.actionNavSplashToNavHome()
                else SplashScreenDirections.actionNavSplashToNavLogin()

                val navOption = NavOptions.Builder().setPopUpTo(R.id.nav_home, true).build()

                findNavController().navigate(action, navOption)
            }
        }, 1500)
    }
}