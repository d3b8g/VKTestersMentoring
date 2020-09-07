package net.d3b8g.vktestersmentoring

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import net.d3b8g.vktestersmentoring.prefs.pMineName
import net.d3b8g.vktestersmentoring.prefs.pMineVisits
import net.d3b8g.vktestersmentoring.ui.splashscreen.SplashScreen

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mineName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if(hadSplash(this,null))
        setContentView(R.layout.activity_main)
        PreferenceManager.getDefaultSharedPreferences(this).apply {
            if(!getBoolean("make_splash",false)){
                startActivity(Intent(this@MainActivity, SplashScreen::class.java))
            }
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_bugs, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        val headerLayoutInflater = navView.getHeaderView(0)
        mineName = headerLayoutInflater.findViewById(R.id.main_user_name)
        val mineVisits = headerLayoutInflater.findViewById<TextView>(R.id.main_user_visits)
        mineName.text = pMineName(this,false,null)

        pMineVisits(this,true)
        mineVisits.text = "Вы посетили приложение: ${pMineVisits(this,false)}"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        mineName.text = pMineName(this,false,null)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}