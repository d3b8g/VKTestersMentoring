package net.d3b8g.vktestersmentoring.ui.notes

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.gson.Gson

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved
 
This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

object Notes {

    fun Context.getNotesJson(): NoteModelFull? {
        val prefValue: String? = PreferenceManager.getDefaultSharedPreferences(this).getString(
            preferenceNotesString, null)
        return Gson().fromJson(prefValue, NoteModelFull::class.java)
    }

    fun Context.getNotesAmount(): Int = getNotesJson()?.count ?: 0

    fun Context.saveNotesJson(notesJson: String) = PreferenceManager.getDefaultSharedPreferences(this).edit {
        putString(preferenceNotesString, notesJson)
    }

    fun Context.addNote(note: NoteModel) {
        val noteJson = getNotesJson()!!
        note.id = noteJson.count
        noteJson.notes.add(note)
        noteJson.count = noteJson.count + 1
        Log.e("Notes", Gson().toJson(noteJson))
        saveNotesJson(Gson().toJson(noteJson))
    }

    fun Context.removeNoteAtIndex(index: Int) {
        val noteJson = getNotesJson()!!
        noteJson.notes.apply {
            removeAt(index)
            mapIndexed { index, noteModel -> noteModel.id = index }
        }
        noteJson.count = noteJson.count - 1
        Log.e("Notes", Gson().toJson(noteJson))
        saveNotesJson(Gson().toJson(noteJson))
    }

    private const val preferenceNotesString = "vktm_notes"
    data class NoteModelFull (
        var notes: ArrayList<NoteModel>,
        var count: Int
    )
    data class NoteModel (
        var id: Int,
        val title: String,
        val description: String,
        val date_of_create: String
    )
}