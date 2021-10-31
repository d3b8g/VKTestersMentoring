package net.d3b8g.vktestersmentoring.ui.notes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonParser
import net.d3b8g.vktestersmentoring.R


class NotesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var notes: ArrayList<NoteModule> = ArrayList()

    fun updateArray(ct: Context) {
        notes.clear()
        PreferenceManager.getDefaultSharedPreferences(ct).apply {
            if (getString("my_notes","")!!.isNotEmpty()) {
                JsonParser.parseString(getString("my_notes","")).asJsonObject.getAsJsonArray("notes").forEach {
                    val note = NoteModule(
                        id = it.asJsonObject.get("id").asInt,
                        title = it.asJsonObject.get("title").asString,
                        description = it.asJsonObject.get("description").asString,
                        date_of_create = it.asJsonObject.get("date_of_create").asString
                    )
                    notes.add(note)
                }
            }
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

        var title: TextView = itemView.findViewById(R.id.note_titile)
        var description: TextView = itemView.findViewById(R.id.note_descr)

        fun bind(inf: NoteModule) {
            title.text = inf.title.replace("&#34;","\"")
            description.text = inf.description.replace("&#34;","\"")

            itemView.setOnClickListener {
                val clipBoard = itemView.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val data = "{ \"id\":-1, \"title\":\"${inf.title}\", \"description\":\"${inf.description}\", \"date_of_create\":\"${inf.date_of_create}\" }"
                val clip: ClipData = ClipData.newPlainText("VKTM-1", data)
                clipBoard.setPrimaryClip(clip)
                Snackbar.make(itemView, "Заметка скопирована", Snackbar.LENGTH_LONG).show()
            }

            itemView.setOnLongClickListener {
                var urData: String
                PreferenceManager.getDefaultSharedPreferences(itemView.context).apply {
                    urData = when (JsonParser.parseString(getString("my_notes","")).asJsonObject.get("count").asInt) {
                        1 -> ""
                        adapterPosition + 1 -> getString("my_notes","")!!.generateStringfyJson(inf, true)
                        else -> getString("my_notes","")!!.generateStringfyJson(inf, false)
                    }
                }

                PreferenceManager.getDefaultSharedPreferences(itemView.context).edit {
                    putString("my_notes", urData)
                }

                updateArray(itemView.context)

                true
            }
        }

        private fun String.generateStringfyJson(inf: NoteModule, isLast: Boolean): String {
            replace("{ \"id\":${inf.id}, \"title\":\"${inf.title}\", \"description\":\"${inf.description}\", \"date_of_create\":\"${inf.date_of_create}\" } ,","")
            replace("\"count\": ${JsonParser.parseString(this).asJsonObject.get("count").asInt}","\"count\": ${JsonParser.parseString(this).asJsonObject.get("count").asInt-1}")
            return if (isLast) {
                ",$this"
            } else {
                this
            }
        }
    }
}