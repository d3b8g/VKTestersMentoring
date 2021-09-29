package net.d3b8g.vktestersmentoring.ui.login


import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.d3b8g.vktestersmentoring.R
import androidx.preference.PreferenceManager
import net.d3b8g.vktestersmentoring.MainActivity

class SplashScreen : Fragment(R.layout.splash_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).actionBar?.hide()
        Handler().postDelayed({
            PreferenceManager.getDefaultSharedPreferences(requireContext()).apply {
                val action = if (getInt("active_user_id", -1) > 0) SplashScreenDirections.actionNavSplashToNavHome()
                else SplashScreenDirections.actionNavSplashToNavLogin()

                findNavController().navigate(action)
            }
        }, 1500)
    }
}