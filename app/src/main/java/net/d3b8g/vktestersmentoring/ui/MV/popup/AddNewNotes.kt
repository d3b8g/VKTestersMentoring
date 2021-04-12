package net.d3b8g.vktestersmentoring.ui.MV.popup

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonParser
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.interfaces.UpdateNotes

class AddNewNotes(val ct: Context,var tyty:UpdateNotes) {
    fun show(){
        val frame = Dialog(ct)
        frame.setContentView(R.layout.alert_add_notes)
        frame.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.FILL_PARENT)
        frame.window!!.setGravity(Gravity.CENTER)

        frame.setCanceledOnTouchOutside(true)

        var title = frame.findViewById<TextInputEditText>(R.id.title_note)
        var descr = frame.findViewById<TextInputEditText>(R.id.descr_note)

        var send = frame.findViewById<Button>(R.id.save_note)

        send.setOnClickListener {
            if(title.text!!.isNotEmpty() && descr.text!!.isNotEmpty()){
                var titleR = title.text.toString().replace("\"","&#34;")
                var descrR = descr.text.toString().replace("\"","&#34;")
                var data = ""
                PreferenceManager.getDefaultSharedPreferences(ct).apply {
                    data = if(getString("my_notes","")!=""){
                        var count = JsonParser().parse(getString("my_notes","")).asJsonObject.get("count").asInt
                        getString("my_notes","")!!.replaceAfter("]",",").replace("]","") +  getString("my_notes","")!!.replace(getString("my_notes","")!!,
                            "{ \"id\":${count}, \"title\":\"${titleR}\", \"description\":\"${descrR}\", \"date_of_create\":\"2020-12-12\" }], \"count\": ${count+1}}")
                    }else{
                        "{ \"notes\": [ { \"id\":0, \"title\":\"${titleR}\", \"description\":\"${descrR}\", \"date_of_create\":\"2020-12-12\" } ], \"count\": 1 }"
                    }
                }
                Log.e("RRR",data)
                PreferenceManager.getDefaultSharedPreferences(ct).edit {
                    putString("my_notes", data)
                }
                tyty.updateNotes()
                frame.dismiss()
            }else{
                Toast.makeText(ct,"Вы не заполнили одно из полей",Toast.LENGTH_SHORT)
            }
        }

        frame.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        frame.show()
    }
}