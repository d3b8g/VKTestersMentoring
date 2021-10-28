package net.d3b8g.vktestersmentoring.ui.notes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParser
import net.d3b8g.vktestersmentoring.R


class NotesAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var notes: ArrayList<NoteModule> = ArrayList()

    fun updateArray(ct:Context){
        notes.clear()
        //var data_example = "{ \"notes\": [ { \"id\":0, \"title\":\"ТайтлЗаметки\", \"description\":\"Описание какой-то заметки, короче говоря короткая инфа с заметки твоей\", \"date_of_create\":\"2020-12-12\" }, { \"id\":1, \"title\":\"ТайтлЗаметки\", \"description\":\"Описание какой-то заметки, короче говоря короткая инфа с заметки твоей\", \"date_of_create\":\"2020-12-12\" }, { \"id\":2, \"title\":\"ТайтлЗаметки\", \"description\":\"Описание какой-то заметки, короче говоря короткая инфа с заметки твоей\", \"date_of_create\":\"2020-12-12\" }, { \"id\":3, \"title\":\"ТайтлЗаметки\", \"description\":\"Описание какой-то заметки, короче говоря короткая инфа с заметки твоей\", \"date_of_create\":\"2020-12-12\" }, { \"id\":4, \"title\":\"ТайтлЗаметки\", \"description\":\"Описание какой-то заметки, короче говоря короткая инфа с заметки твоей\", \"date_of_create\":\"2020-12-12\" } ], \"count\": 5}"
        PreferenceManager.getDefaultSharedPreferences(ct).apply {
            if (getString("my_notes","") != "") {
                Log.e("RRR","construction:${JsonParser.parseString(getString("my_notes",""))}" )
                JsonParser.parseString(getString("my_notes","")).asJsonObject.getAsJsonArray("notes").forEach {
                    Log.e("RRR","construction:${it} \n dest: ${getString("my_notes","")}" )
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

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class NotesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title = itemView.findViewById<TextView>(R.id.note_titile)
        var descr = itemView.findViewById<TextView>(R.id.note_descr)

        fun bind(inf: NoteModule){
            title.text = inf.title.replace("&#34;","\"")
            descr.text = inf.description.replace("&#34;","\"")
            itemView.setOnClickListener {
                val clipBoard = itemView.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val data = "{ \"id\":-1, \"title\":\"${inf.title}\", \"description\":\"${inf.description}\", \"date_of_create\":\"${inf.date_of_create}\" }"
                val clip: ClipData = ClipData.newPlainText("VKTM-1", data)
                clipBoard.setPrimaryClip(clip)
                Toast.makeText(itemView.context,"Заметка скопирована",Toast.LENGTH_LONG).show()
            }

            itemView.setOnLongClickListener {
                var urData: String
                PreferenceManager.getDefaultSharedPreferences(itemView.context).apply {
                    urData = when (JsonParser.parseString(getString("my_notes","")).asJsonObject.get("count").asInt) {
                        1 -> ""
                        adapterPosition + 1 -> getString("my_notes","")!!.replace(",{ \"id\":${inf.id}, \"title\":\"${inf.title}\", \"description\":\"${inf.description}\", \"date_of_create\":\"${inf.date_of_create}\" }","").replace("\"count\": ${JsonParser.parseString(getString("my_notes","")).asJsonObject.get("count").asInt}","\"count\": ${JsonParser().parse(getString("my_notes","")).asJsonObject.get("count").asInt-1}")
                        else -> getString("my_notes","")!!.replace("{ \"id\":${inf.id}, \"title\":\"${inf.title}\", \"description\":\"${inf.description}\", \"date_of_create\":\"${inf.date_of_create}\" } ,","").replace("\"count\": ${JsonParser.parseString(getString("my_notes","")).asJsonObject.get("count").asInt}","\"count\": ${JsonParser().parse(getString("my_notes","")).asJsonObject.get("count").asInt-1}")
                    }
                }

                PreferenceManager.getDefaultSharedPreferences(itemView.context).edit {
                    putString("my_notes",urData)
                }

                updateArray(itemView.context)

                true
            }
        }
    }
}