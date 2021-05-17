package net.d3b8g.vktestersmentoring.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.adapters.NotesAdapter
import net.d3b8g.vktestersmentoring.interfaces.UpdateNotes
import net.d3b8g.vktestersmentoring.ui.MV.popup.AddNewNotes
import net.d3b8g.vktestersmentoring.ui.MV.popup.ImportNotes

class NotesFragment : Fragment(), UpdateNotes {

    lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_nots,container,false)

        var add = inflate.findViewById<Button>(R.id.add_notes)
        var import = inflate.findViewById<Button>(R.id.import_notes)
        var rcv = inflate.findViewById<RecyclerView>(R.id.rcv_notes)

        adapter = NotesAdapter()
        rcv.adapter = adapter
        rcv.layoutManager = LinearLayoutManager(inflater.context, RecyclerView.VERTICAL,false)
        rcv.setHasFixedSize(true)
        adapter.updateArray(inflate.context)

        add.setOnClickListener {
            AddNewNotes(inflate.context,this).show()
        }
        import.setOnClickListener {
            ImportNotes(inflate.context,this).show()
        }

        return inflate
    }

    override fun updateNotes() {
        adapter.updateArray(requireContext())
    }
}