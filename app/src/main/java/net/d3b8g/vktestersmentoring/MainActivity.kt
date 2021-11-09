package net.d3b8g.vktestersmentoring

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.coroutines.*
import net.d3b8g.vktestersmentoring.customUI.mediaCenter.FragmentMediaCenter
import net.d3b8g.vktestersmentoring.db.UserData.UserData
import net.d3b8g.vktestersmentoring.db.UserData.UserDatabase
import net.d3b8g.vktestersmentoring.helper.ToolsShit.appLog
import net.d3b8g.vktestersmentoring.helper.UITypes
import net.d3b8g.vktestersmentoring.interfaces.UpdateMainUI

class MainActivity : AppCompatActivity(R.layout.activity_main), UpdateMainUI {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navBar: ChipNavigationBar
    private val mediaCenter: FragmentMediaCenter by lazy {
        findViewById(R.id.media_center)
    }

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }
    private val userDatabase by lazy { UserDatabase.getInstance(this).userDatabaseDao }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navBar = findViewById(R.id.bottom_navigation_menu)
        navBar.setItemSelected(R.id.home, true)

        navBar.setOnClickListener {
            val history = navController.currentDestination?.id ?: false
            if (history != R.id.nav_splash) {
                navBar.setOnItemSelectedListener {
                    when (it) {
                        R.id.profile -> navController.navigate(R.id.nav_profile)
                        R.id.home -> navController.navigate(R.id.nav_main)
                        R.id.notes -> navController.navigate(R.id.nav_notes)
                    }
                }
            }
        }

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            if (getInt("active_user_id", -1) < 1) navBar.visibility = View.GONE
            else uid = getInt("active_user_id", -1)
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val user = getUser()
            updateVisitsCounter(user.counter + 1)
        }
    }

    override fun updateUI(type: UITypes) {
        when(type) {
            UITypes.SHOW_TABBAR -> navBar.visibility = View.VISIBLE
            UITypes.HIDE_TABBAR -> navBar.visibility = View.GONE
            UITypes.NEW_USER -> {
                navBar.visibility = View.VISIBLE
                navBar.setItemSelected(R.id.home, true)
            }
            UITypes.AVATAR -> {}
        }
    }

    override fun launchMediaCenter() {
        FragmentMediaCenter.recording_anim = true
        mediaCenter.apply {
            visibility = View.VISIBLE
            startReproduceAudio()
        }
    }

    private suspend fun getUser(): UserData = withContext(Dispatchers.IO) {
        return@withContext userDatabase.getUserById(uid)
    }

    private suspend fun updateVisitsCounter(visits: Int) = withContext(Dispatchers.IO) {
        userDatabase.updateVisitsCounter(visits)
    }

    override fun onDestroy() {
        lifecycleScope.launch { updateVisitsCounter(getUser().counter + 1) }
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val history = navController.currentDestination?.id ?: false
        val excludeNav = arrayListOf(R.id.nav_main, R.id.nav_splash, R.id.nav_notes, R.id.nav_login)
        if (!excludeNav.contains(history)) navController.popBackStack()
        else {
            Snackbar.make(findViewById(R.id.container), "Закрыть приложение?", Snackbar.LENGTH_SHORT)
                .setAction("Закрыть") {
                    finishAffinity()
                }
                .show()
        }
    }

    companion object {
        var mainState: Int? = null
        var uid = 1
    }
}