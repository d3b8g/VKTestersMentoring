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
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.coroutines.*
import net.d3b8g.vktestersmentoring.db.UserData.UserData
import net.d3b8g.vktestersmentoring.db.UserData.UserDatabase
import net.d3b8g.vktestersmentoring.helper.UITypes
import net.d3b8g.vktestersmentoring.interfaces.UpdateMainUI
import net.d3b8g.vktestersmentoring.ui.longread.MediaCenter
import net.d3b8g.vktestersmentoring.ui.longread.MediaCenter.Companion.recording_anim

class MainActivity : AppCompatActivity(), net.d3b8g.vktestersmentoring.ui.dicto.MediaCenterInterface, UpdateMainUI {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mCenter: FrameLayout
    lateinit var navBar: ChipNavigationBar

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }
    private val userDatabase by lazy { UserDatabase.getInstance(this).userDatabaseDao }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navBar = findViewById(R.id.bottom_navigation_menu)
        navBar.setItemSelected(R.id.home, true)

        navBar.setOnItemSelectedListener {
            when (it) {
                R.id.home -> {
                    if (mainState == null) navController.navigate(R.id.nav_main)
                }
                R.id.notes -> navController.navigate(R.id.nav_notes)
            }
        }

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            if (getInt("active_user_id", -1) < 1) navBar.visibility = View.GONE
        }
    }

    override fun updateUI(type: UITypes) {
        if (type == UITypes.SHOW_TABBAR) {
            navBar.visibility = View.VISIBLE
        }
    }

    private suspend fun getUser(): UserData = withContext(Dispatchers.IO) {
        return@withContext userDatabase.getUserById(1)
    }

    private suspend fun updateVisitsCounter(visits: Int) = withContext(Dispatchers.IO) {
        userDatabase.updateVisitsCounter(visits)
    }

    private fun titleStatus(count: Int?): String = when {
        count in 5..20 -> "раз"
        count.toString().takeLast(1) == "l" -> "1 раз"
        else -> {
            when (count.toString().takeLast(1).toInt()) {
                in 0..1 -> "раз"
                in 2..4 -> "раза"
                in 5..9 -> "раз"
                else -> "раз"
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun startRecordingComponents() {
        recording_anim = true
        val transaction = (this as FragmentActivity).supportFragmentManager.beginTransaction()
        transaction.replace(mCenter.id, MediaCenter()).commit()
        mCenter.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        val history = navController.currentDestination?.id ?: false
        val excludeNav = arrayListOf(R.id.nav_main, R.id.nav_splash, R.id.nav_notes)
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
        var mainState: NavDirections? = null
        var uid = 1
        var visits = 0
    }
}