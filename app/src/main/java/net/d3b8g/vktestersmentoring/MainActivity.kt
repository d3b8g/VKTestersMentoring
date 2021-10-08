package net.d3b8g.vktestersmentoring

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import net.d3b8g.vktestersmentoring.db.UserData.UserData
import net.d3b8g.vktestersmentoring.db.UserData.UserDatabase
import net.d3b8g.vktestersmentoring.interfaces.ActionBar
import net.d3b8g.vktestersmentoring.interfaces.UpdateMainUI
import net.d3b8g.vktestersmentoring.modules.UITypes
import net.d3b8g.vktestersmentoring.ui.home.HomeFragment
import net.d3b8g.vktestersmentoring.ui.home.HomeFragmentDirections
import net.d3b8g.vktestersmentoring.ui.home.MediaCenter
import net.d3b8g.vktestersmentoring.ui.home.MediaCenter.Companion.recording_anim
import net.d3b8g.vktestersmentoring.ui.login.LoginFragmentDirections
import net.d3b8g.vktestersmentoring.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity(), net.d3b8g.vktestersmentoring.interfaces.MediaCenter, UpdateMainUI, ActionBar {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mineName: TextView
    lateinit var mCenter: FrameLayout
    lateinit var navView: NavigationView
    lateinit var headerLayoutInflater: View
    lateinit var mineVisits: TextView
    lateinit var userImage: CircleImageView

    private var job = Job()
    private var scope = CoroutineScope(Dispatchers.Main + job)

    private val userDatabase by lazy { UserDatabase.getInstance(this).userDatabaseDao }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        mCenter = findViewById(R.id.media_center)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_bugs,
                R.id.nav_slideshow,
                R.id.nav_dictophone,
                R.id.nav_upload,
                R.id.nav_mv,
                R.id.nav_notes,
                R.id.nav_conf
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        actionBarChange(true)

        headerLayoutInflater = navView.getHeaderView(0)
        mineName = headerLayoutInflater.findViewById(R.id.main_user_name)
        mineVisits = headerLayoutInflater.findViewById(R.id.main_user_visits)
        userImage = headerLayoutInflater.findViewById(R.id.main_user_avatar)

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            getInt("active_user_id", -1).let {
                uid = if (it > 0) {
                    updateUI(UITypes.ALL_DATA)
                    it
                } else {
                    1
                }
            }
        }
    }

    override fun updateUI(type: String) {
        scope.launch {
            val user = getUser()

            when (true) {
                (type == UITypes.ALL_DATA || type == UITypes.AVATAR) && user.avatar.isNotEmpty()-> {
                    Picasso.get().load(user.avatar).resize(150, 150).into(userImage)
                }
                type == UITypes.ALL_DATA || type == UITypes.USERNAME -> {
                    mineName.text = user.username
                }
                type == UITypes.ALL_DATA || type == UITypes.VISITS -> {
                    user.counter.let {
                        setScoreVisit(it)
                        visits = it + 1
                    }
                }
            }
        }
    }

    private suspend fun getUser(): UserData = withContext(Dispatchers.IO) {
        return@withContext userDatabase.getUserById(1)
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

    @SuppressLint("SetTextI18n")
    private fun setScoreVisit(count: Int) {
        mineVisits.text = "Вы посетили приложение: $count ${titleStatus(count)}"
        when(count){
            in 51..100 -> {
                mineVisits.setTextColor(Color.parseColor("#8b00ff"))
            }
            in 101..150 -> {
                mineVisits.setTextColor(Color.parseColor("#fff211"))
            }
            in 151..200 -> {
                mineVisits.setTextColor(Color.parseColor("#18ff0f"))
            }
            in 201..250 -> {
                mineVisits.setTextColor(Color.parseColor("#321eff"))
            }
            in 251..300 -> {
                mineVisits.setTextColor(Color.parseColor("#ff77ea"))
            }
            in 300..1900 -> {
                mineVisits.setTextColor(Color.parseColor("#e13100"))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> {
                val action = HomeFragmentDirections.actionNavHomeToNavSettings()
                this.findNavController(R.id.nav_host_fragment).navigate(action)
                actionBarChange(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        setScoreVisit(visits)
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

    override fun actionBarChange(hide: Boolean) {
        if (hide) supportActionBar?.hide()
        else supportActionBar?.show()
    }

    companion object {
        var uid = 1
        var visits = 0
    }
}