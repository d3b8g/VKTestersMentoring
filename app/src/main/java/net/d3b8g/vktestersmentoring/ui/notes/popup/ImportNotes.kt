package net.d3b8g.vktestersmentoring.ui.notes.popup

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonParser
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.helper.ToolsShit.appLog
import net.d3b8g.vktestersmentoring.helper.ToolsShit.isDevicesDarkTheme
import net.d3b8g.vktestersmentoring.ui.notes.Notes
import net.d3b8g.vktestersmentoring.ui.notes.Notes.addNote
import net.d3b8g.vktestersmentoring.ui.notes.Notes.saveNotesJson
import net.d3b8g.vktestersmentoring.ui.notes.UpdateNotesInterface

class ImportNotes(val ct: Context, private val updateNotesUI: UpdateNotesInterface) {

    fun show() {
        val frame = Dialog(ct)
        frame.setContentView(R.layout.alert_import)
        frame.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        frame.window!!.setGravity(Gravity.BOTTOM)

        if (ct.isDevicesDarkTheme()) {
            frame.findViewById<LinearLayout>(R.id.modal_view_import_notes).run {
                backgroundTintList = AppCompatResources.getColorStateList(ct, R.color.colorBlack)
            }
        }

        frame.setCanceledOnTouchOutside(true)

        val titleInput = frame.findViewById<TextInputEditText>(R.id.import_r)

        val send = frame.findViewById<Button>(R.id.import_note)

        send.setOnClickListener {
            if (titleInput.text!!.isNotEmpty()) {
                try {
                    val newNote = Gson().fromJson(titleInput.text.toString(), Notes.NoteModel::class.java)
                    ct.addNote(newNote)

                    updateNotesUI.updateNotes()
                }catch (e: Exception) {
                    Toast.makeText(ct,"Невалидный параметр", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(ct,"Невалидный параметр", Toast.LENGTH_SHORT).show()
            }
        }

        frame.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        frame.setCanceledOnTouchOutside(true)
        frame.show()
    }
}