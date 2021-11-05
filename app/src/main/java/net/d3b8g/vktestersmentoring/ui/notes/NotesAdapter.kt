package net.d3b8g.vktestersmentoring.ui.notes

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonParser
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.ui.notes.Notes.getNotesJson
import net.d3b8g.vktestersmentoring.ui.notes.Notes.removeNoteAtIndex


class NotesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var notes: ArrayList<Notes.NoteModel> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun updateArray(ct: Context) {
        val notesJson = ct.getNotesJson()
        notesJson?.let {
            notes.clear()
            notes.addAll(it.notes)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_note, parent, false)
        return NotesAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is NotesAdapterViewHolder) holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    inner class NotesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var title: TextView = itemView.findViewById(R.id.note_titile)
        private var description: TextView = itemView.findViewById(R.id.note_descr)

        fun bind(inf: Notes.NoteModel) {

            title.text = inf.title
            description.text = inf.description

            itemView.setOnClickListener {
                val clipBoard = itemView.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val jsonStringfy = Gson().toJson(inf)
                val clip: ClipData = ClipData.newPlainText("VKTM-1", jsonStringfy)
                clipBoard.setPrimaryClip(clip)
                Snackbar.make(itemView, "Заметка скопирована", Snackbar.LENGTH_LONG).show()
            }

            itemView.setOnLongClickListener {
                itemView.context.removeNoteAtIndex(adapterPosition)
                updateArray(itemView.context)
                true
            }
        }
    }
}