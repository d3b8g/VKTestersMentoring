package net.d3b8g.vktestersmentoring.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE

val minePath = "UserSetup"
val lgPath = "LonggridSetup"

fun pMineName(ct:Context,func:Boolean,item:String?):String? {
    val prefs = ct.getSharedPreferences(minePath, MODE_PRIVATE)

    return if(func){
        var bt = prefs.edit()
        bt.putString("mine_name",item!!).apply()
        null
    }else{
        prefs.getString("mine_name","undefind null")
    }
}
fun pMineVisits(ct:Context,func:Boolean):Int? {
    val prefs = ct.getSharedPreferences(minePath, MODE_PRIVATE)
    return if(func){
        var bt = prefs.edit()
        bt.putInt("mine_visits", (pMineVisits(ct,false)!!+1)).apply()
        null
    }else{
        prefs.getInt("mine_visits",0)
    }
}

data class paramCt(var id:String,var value:Boolean)

fun setReadParam(ct:Context,param:paramCt){
    val pref = ct.getSharedPreferences("Settings",MODE_PRIVATE).edit()
    pref.putBoolean(param.id,param.value).apply()
}
fun getReadParam(ct:Context,param:String) : Boolean{
    val pref = ct.getSharedPreferences("Settings", MODE_PRIVATE)
    return pref.getBoolean(param,false)
}

data class paramCtQ(var id:String,var value:Int)

fun setQualityParam(ct:Context,param:paramCtQ){
    val pref = ct.getSharedPreferences("Settings",MODE_PRIVATE).edit()
    pref.putInt(param.id,param.value).apply()
}
fun getQualityParam(ct:Context,param:String) : Int{
    val pref = ct.getSharedPreferences("Settings", MODE_PRIVATE)
    return pref.getInt(param,0)
}


