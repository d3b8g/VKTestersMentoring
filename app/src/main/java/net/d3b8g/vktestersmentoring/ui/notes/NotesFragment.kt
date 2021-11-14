package net.d3b8g.vktestersmentoring.ui.notes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.databinding.FragmentNotsBinding
import net.d3b8g.vktestersmentoring.ui.notes.popup.AddNewNotes
import net.d3b8g.vktestersmentoring.ui.notes.popup.ImportNotes

class NotesFragment : Fragment(R.layout.fragment_nots), UpdateNotesInterface {

    lateinit var binding: FragmentNotsBinding
    lateinit var adapter: NotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentNotsBinding.bind(view)

        val add = binding.addNotes
        val importNotes = binding.importNotes
        val rcv = binding.rcvNotes

        adapter = NotesAdapter()
        rcv.adapter = adapter
        rcv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rcv.setHasFixedSize(true)
        adapter.updateArray(requireContext())

        add.setOnClickListener {
            AddNewNotes(requireContext(),this).show()
        }

        importNotes.setOnClickListener {
            ImportNotes(requireContext(),this).show()
        }
    }

    override fun updateNotes() {
        adapter.updateArray(requireContext())
    }

}