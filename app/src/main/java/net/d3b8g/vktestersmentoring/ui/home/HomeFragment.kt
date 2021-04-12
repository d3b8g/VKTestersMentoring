package net.d3b8g.vktestersmentoring.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.adapters.LongGridAdapter
import net.d3b8g.vktestersmentoring.interfaces.LonggridCall
import net.d3b8g.vktestersmentoring.modules.LongGridModule
import net.d3b8g.vktestersmentoring.prefs.*

class HomeFragment : Fragment(),LonggridCall {

    lateinit var adapter:LongGridAdapter

    lateinit var title:TextView
    lateinit var cbRead: CheckBox
    lateinit var qProgress:ProgressBar
    lateinit var open:Button
    lateinit var block:LinearLayout
    lateinit var plug:TextView
    lateinit var progress_text:TextView
    var displayWidth = 0

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_longgrid, container, false)
        val rcv = root.findViewById<RecyclerView>(R.id.rcv_longrid)
        adapter = LongGridAdapter(this)
        rcv.adapter = adapter
        rcv.layoutManager = LinearLayoutManager(inflater.context, RecyclerView.HORIZONTAL,false)
        rcv.setHasFixedSize(true)
        adapter.update(root.context)

        title = root.findViewById(R.id.lg_title)
        cbRead = root.findViewById(R.id.lg_had_read)
        qProgress= root.findViewById(R.id.lg_progress)
        open = root.findViewById(R.id.lg_open)
        block = root.findViewById(R.id.lg_components_block)
        plug = root.findViewById(R.id.plug_text)
        progress_text = root.findViewById(R.id.lg_progress_text)

        displayWidth = requireActivity().windowManager.defaultDisplay.width

        return root
    }

    override fun changeParam(item: LongGridModule,pos:Int) {
        plug.visibility = View.GONE
        block.visibility = View.VISIBLE
        open.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
            ContextCompat.startActivity(requireActivity(), browserIntent, null)
        }
        title.text = item.title
        cbRead.isChecked = item.hadRead
        progress_text.text = "${item.quality}/10"
        qProgress.progress =  item.quality
        cbRead.setOnClickListener {
            setReadParam(requireActivity(), paramCt("check_box_$pos",cbRead.isChecked))
            adapter.update(requireContext())
            if(getCountReads(requireActivity())==12) Toast.makeText(requireContext(),"Поздравляю, ты прочитал все статьи и стал отважным джедаеm!",Toast.LENGTH_SHORT).show()
        }
        qProgress.setOnTouchListener { _, event ->
            if(event.action == MotionEvent.ACTION_DOWN){
                qProgress.progress = event.x.toInt()*10/(qProgress.width-20)
                progress_text.text = "${event.x.toInt()*10/(qProgress.width-20)}/10"
                setQualityParam(requireActivity(), paramCtQ("quality_grid_$pos",event.x.toInt()*10/(qProgress.width-20)))
                adapter.update(requireContext())
            }
            true
        }

    }
}