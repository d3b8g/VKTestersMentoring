package net.d3b8g.vktestersmentoring.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE

val minePath = "UserSetup"
val lgPath = "LonggridSetup"

data class paramCt(var id: String, var value: Boolean)

fun setReadParam(ct: Context, param: paramCt) {
    val pref = ct.getSharedPreferences("Settings", MODE_PRIVATE).edit()
    pref.putBoolean(param.id, param.value).apply()
}
fun getReadParam(ct:Context, param:String): Boolean {
    val pref = ct.getSharedPreferences("Settings", MODE_PRIVATE)
    return pref.getBoolean(param,false)
}
fun getCountReads(ct:Context) : Int {
    var returnBack = 0
    for (i in 0..11) if(getReadParam(ct,"check_box_$i")) returnBack++
    return returnBack
}

data class paramCtQ(var id: String, var value: Int)

fun setQualityParam(ct: Context, param: paramCtQ) {
    val pref = ct.getSharedPreferences("Settings", MODE_PRIVATE).edit()
    pref.putInt(param.id,param.value).apply()
}
fun getQualityParam(ct: Context, param: String): Int {
    val pref = ct.getSharedPreferences("Settings", MODE_PRIVATE)
    return pref.getInt(param,0)
}


