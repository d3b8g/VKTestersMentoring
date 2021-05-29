package net.d3b8g.vktestersmentoring.ui.MV.popup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonParser
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.interfaces.UpdateNotes

class ImportNotes(val ct: Context, var tyty: UpdateNotes) {
    fun show() {
        val frame = Dialog(ct)
        frame.setContentView(R.layout.alert_import)
        frame.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.FILL_PARENT)
        frame.window!!.setGravity(Gravity.BOTTOM)

        frame.setCanceledOnTouchOutside(true)

        var titleInput = frame.findViewById<TextInputEditText>(R.id.import_r)

        var send = frame.findViewById<Button>(R.id.import_note)

        send.setOnClickListener {
            if(titleInput.text!!.isNotEmpty()) {
                var data = ""
                PreferenceManager.getDefaultSharedPreferences(ct).apply {
                    try {
                        var yu = JsonParser.parseString(titleInput.text.toString()).asJsonObject
                        var title = yu.asJsonObject.get("title").asString
                        var descr = yu.asJsonObject.get("description").asString
                        var dateTime = yu.asJsonObject.get("date_of_create").asString
                        if(title.length < 51) {
                            data = if(getString("my_notes","") != ""){
                                var count = JsonParser.parseString(getString("my_notes","")).asJsonObject.get("count").asInt
                                getString("my_notes","")!!.replaceAfter("]",",").replace("]","") +  getString("my_notes","")!!.replace(getString("my_notes","")!!,
                                    "{ \"id\":${count}, \"title\":\"${title}\", \"description\":\"${descr}\", \"date_of_create\":\"${dateTime}\" }], \"count\": ${count+1}}")
                            }else {
                                "{ \"notes\": [ { \"id\":0, \"title\":\"${title}\", \"description\":\"${descr}\", \"date_of_create\":\"${dateTime}\" } ], \"count\": 1 }"
                            }
                        } else {
                            Toast.makeText(ct,"Заголовок содержит больше 50 символов.", Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: Exception) {
                        Toast.makeText(ct,"Неопознанная JSON-схема", Toast.LENGTH_SHORT).show()
                    }
                }
                if(data!="") {
                    PreferenceManager.getDefaultSharedPreferences(ct).edit {
                        putString("my_notes",data)
                    }
                    tyty.updateNotes()
                    frame.dismiss()
                }
            }else{
                Toast.makeText(ct,"Невалидный параметр", Toast.LENGTH_SHORT).show()
            }
        }

        frame.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        frame.show()
    }
}