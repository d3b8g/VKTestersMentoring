package net.d3b8g.vktestersmentoring.ui.notes.popup

import android.annotation.SuppressLint
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
import net.d3b8g.vktestersmentoring.ui.notes.UpdateNotesInterface
import java.text.SimpleDateFormat
import java.util.*

class AddNewNotes(val ct: Context, val tyty: UpdateNotesInterface) {

    @SuppressLint("SimpleDateFormat")
    fun show() {
        val frame = Dialog(ct)
        frame.setContentView(R.layout.alert_add_notes)
        frame.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        frame.window!!.setGravity(Gravity.CENTER)

        frame.setCanceledOnTouchOutside(true)

        val title = frame.findViewById<TextInputEditText>(R.id.title_note)
        val descr = frame.findViewById<TextInputEditText>(R.id.descr_note)

        val send = frame.findViewById<Button>(R.id.save_note)

        send.setOnClickListener {
            if (title.text!!.any { text-> text.isLetter() } && descr.text!!.any { text-> text.isLetter()}) {
                val titleR = title.text.toString().replace("\"","&#34;")
                val descrR = descr.text.toString().replace("\"","&#34;")
                var data: String
                PreferenceManager.getDefaultSharedPreferences(ct).apply {
                    data = if (getString("my_notes", "")!!.isNotEmpty()) {
                        val count = JsonParser.parseString(getString("my_notes","")).asJsonObject.get("count").asInt
                        getString("my_notes","")!!.replaceAfter("]",",").replace("]","") +  getString("my_notes","")!!.replace(getString("my_notes","")!!,
                            "{ \"id\":${count}, \"title\":\"${titleR}\", \"description\":\"${descrR}\", \"date_of_create\":\""+ SimpleDateFormat("yyyy/MM/dd").format(Date())+"\" }], \"count\": ${count+1}}")
                    } else {
                        "{ \"notes\": [ { \"id\":0, \"title\":\"${titleR}\", \"description\":\"${descrR}\", \"date_of_create\":\""+ SimpleDateFormat("yyyy/MM/dd").format(Date())+"\" } ], \"count\": 1 }"
                    }
                }
                PreferenceManager.getDefaultSharedPreferences(ct).edit {
                    putString("my_notes", data)
                }
                tyty.updateNotes()
                frame.dismiss()
            } else {
                Toast.makeText(ct,"Вы не заполнили одно из полей", Toast.LENGTH_SHORT).show()
            }
        }

        frame.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        frame.show()
    }
}