package net.d3b8g.vktestersmentoring.ui.login

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import net.d3b8g.vktestersmentoring.R

class Splash_:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        Handler().postDelayed({
            finish()
        },2000)
    }

    override fun onBackPressed() {}

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slide_out_bottom)
    }
}