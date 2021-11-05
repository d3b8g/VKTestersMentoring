package net.d3b8g.vktestersmentoring.ui.longread

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.customUI.fragmentHeader.FragmentHeader
import net.d3b8g.vktestersmentoring.databinding.FragmentLongreadBinding
import net.d3b8g.vktestersmentoring.prefs.*

class LongreadFragment : Fragment(R.layout.fragment_longread), LongreadCall {

    private lateinit var binding: FragmentLongreadBinding
    private lateinit var adapter: LongreadAdapter

    private val fragmentHeader: FragmentHeader by lazy {
        binding.bugsHeader
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentLongreadBinding.bind(view)

        adapter = LongreadAdapter(this)
        binding.rcvLongrid.adapter = adapter
        binding.rcvLongrid.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rcvLongrid.setHasFixedSize(true)
        adapter.update(view.context)

        fragmentHeader.setTitleText("Лонгриды")
        fragmentHeader.setRightButtonIcon(
            ResourcesCompat.getDrawable(resources ,R.drawable.ic_close, resources.newTheme())!!
        )
        fragmentHeader.setRightButtonListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun changeParam(item: LongreadModule, pos: Int) {
        binding.plugText.visibility = View.GONE
        binding.lgComponentsBlock.visibility = View.VISIBLE
        binding.lgOpen.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
            ContextCompat.startActivity(requireActivity(), browserIntent, null)
        }
        binding.lgTitle.text = item.title
        binding.lgHadRead.isChecked = item.hadRead
        binding.lgProgressText.text = "${item.quality}/10"
        binding.lgProgress.progress = item.quality
        binding.lgHadRead.setOnClickListener {
            setReadParam(requireActivity(), paramCt("check_box_$pos", binding.lgHadRead.isChecked))
            adapter.update(requireContext())
            if (getCountReads(requireActivity()) == 12)
                Toast.makeText(
                    requireContext(),
                    "Поздравляю, ты прочитал все статьи и стал отважным джедаеm!",
                    Toast.LENGTH_SHORT
                ).show()
        }

        binding.lgProgress.setOnTouchListener { _, event->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val progressValue = event.x.toInt() * 10 / (binding.lgProgress.width - 20)
                binding.lgProgress.progress = progressValue
                binding.lgProgressText.text = "$progressValue/10"
                setQualityParam(requireActivity(), paramCtQ("quality_grid_$pos", progressValue))
                adapter.update(requireContext())
            }
            true
        }
    }
}