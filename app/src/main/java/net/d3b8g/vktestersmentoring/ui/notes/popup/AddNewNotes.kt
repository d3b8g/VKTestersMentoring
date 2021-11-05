package net.d3b8g.vktestersmentoring.ui.notes.popup

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonParser
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.ui.notes.Notes
import net.d3b8g.vktestersmentoring.ui.notes.Notes.getNotesJson
import net.d3b8g.vktestersmentoring.ui.notes.Notes.saveNotesJson
import net.d3b8g.vktestersmentoring.ui.notes.UpdateNotesInterface
import java.text.SimpleDateFormat
import java.util.*

class AddNewNotes(val ct: Context, private val updateNotesUI: UpdateNotesInterface) {

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
                val notesJson = ct.getNotesJson()
                val pushNotesJson: String = if (notesJson != null) {
                    val jsonCount = notesJson.count
                    notesJson.notes.add(
                        Notes.NoteModel(
                            id = jsonCount,
                            title = title.toString(),
                            description = descr.toString(),
                            date_of_create = SimpleDateFormat("yyyy-MM-dd").format(Date())
                    ))
                    Gson().toJson(Notes.NoteModelFull(notesJson.notes, 0))
                } else {
                    Gson().toJson(Notes.NoteModelFull(
                        arrayListOf(Notes.NoteModel(
                            id = 0,
                            title = title.toString(),
                            description = descr.toString(),
                            date_of_create = SimpleDateFormat("yyyy-MM-dd").format(Date())
                        )), 0
                    ))
                }

                ct.saveNotesJson(pushNotesJson)
                updateNotesUI.updateNotes()
                frame.dismiss()
            } else {
                Toast.makeText(ct,"Вы не заполнили одно из полей", Toast.LENGTH_SHORT).show()
            }
        }

        frame.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        frame.setCanceledOnTouchOutside(true)
        frame.show()
    }
}