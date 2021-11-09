package net.d3b8g.vktestersmentoring.helper

import android.content.Context
import android.content.res.Configuration
import android.util.Log

object ToolsShit {

    fun appLog(myClass: Any, text: String) {
        Log.e(myClass::class.java.simpleName, text)
    }

    fun Context.isDevicesDarkTheme(): Boolean =
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

}