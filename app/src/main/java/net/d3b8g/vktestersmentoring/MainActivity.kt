package net.d3b8g.vktestersmentoring

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
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
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import net.d3b8g.vktestersmentoring.db.CreateUserExist
import net.d3b8g.vktestersmentoring.interfaces.UpdateAvatar
import net.d3b8g.vktestersmentoring.ui.home.MediaCenter
import net.d3b8g.vktestersmentoring.ui.home.MediaCenter.Companion.recording_anim
import net.d3b8g.vktestersmentoring.ui.login.Splash_
import net.d3b8g.vktestersmentoring.ui.settings.Settings


class MainActivity : AppCompatActivity(), net.d3b8g.vktestersmentoring.interfaces.MediaCenter, UpdateAvatar {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mineName: TextView
    lateinit var mCenter: FrameLayout
    lateinit var navView: NavigationView
    lateinit var headerLayoutInflater: View
    lateinit var mineVisits: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this@MainActivity, Splash_::class.java))

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            uid = getInt("active_user_id", 1)
        }

        init()

        visits = getScoreVisits() + 1

    }

    private fun init(){
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

        headerLayoutInflater = navView.getHeaderView(0)
        mineName = headerLayoutInflater.findViewById(R.id.main_user_name)
        mineVisits = headerLayoutInflater.findViewById(R.id.main_user_visits)
        mineName.text = CreateUserExist(this).readUserData(uid)?.username

        val userImage = headerLayoutInflater.findViewById<CircleImageView>(R.id.main_user_avatar)
        Picasso.get().load(getUserImage()).resize(150, 150).into(userImage)

        setScoreVisit(getScoreVisits())
    }

    private fun getScoreVisits(): Int = CreateUserExist(this).readUserData(uid)!!.counter

    private fun getUserImage(): String? = CreateUserExist(this).readUserData(uid)?.avatar

    fun titleForStatus(count: Int?):String{
        return when {
            count in 5..20 -> "раз"
            count.toString().takeLast(1)=="l" -> "1 раз"
            else -> {
                when (count.toString().takeLast(1).toInt()) {
                    in 0..1 -> "раз"
                    in 2..4 -> "раза"
                    in 5..9 -> "раз"
                    else -> "раз"
                }
            }
        }
    }

    private fun setScoreVisit(count: Int) {
        mineVisits.text = "Вы посетили приложение: $count ${titleForStatus(count)}"
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_settings -> {
                startActivity(Intent(this, Settings::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        setScoreVisit(visits)
    }

    override fun onPause() {
        super.onPause()
        CreateUserExist(this).updateCountVisits(uid.toString(), visits)
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

    override fun updateAvatar() {
        val userImage = headerLayoutInflater.findViewById<CircleImageView>(R.id.main_user_avatar)
        Picasso.get().load(getUserImage()).resize(150, 150).into(userImage)
    }

    companion object{
        var uid = 1
        var visits = 0
    }
}