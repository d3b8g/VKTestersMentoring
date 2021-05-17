package net.d3b8g.vktestersmentoring.ui.settings

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.android.material.switchmaterial.SwitchMaterial
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.ui.login.LoginActivity

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_settings)

        val tracking = findViewById<SwitchMaterial>(R.id.s_tracking)
        val avatar = findViewById<SwitchMaterial>(R.id.s_avatar)
        val logout = findViewById<Button>(R.id.logout)

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            tracking.isChecked = getBoolean("do_tracking", false)
            avatar.isChecked = getBoolean("do_avatar", false)
        }
        tracking.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(this).edit {
                putBoolean("do_tracking", tracking.isChecked)
            }
        }
        avatar.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(this).edit {
                putBoolean("do_avatar", avatar.isChecked)
            }
        }
        logout.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(this).edit {
                putBoolean("make_splash", false)
            }
            startActivity(Intent(this, LoginActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
            finish()
        }
    }
}