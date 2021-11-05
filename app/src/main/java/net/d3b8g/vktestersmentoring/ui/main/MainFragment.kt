package net.d3b8g.vktestersmentoring.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.d3b8g.vktestersmentoring.MainActivity
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.databinding.FragmentMainBinding
import net.d3b8g.vktestersmentoring.db.UserData.UserData
import net.d3b8g.vktestersmentoring.db.UserData.UserDatabase
import net.d3b8g.vktestersmentoring.helper.DateHelper.getMonth
import net.d3b8g.vktestersmentoring.helper.StringCase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved
 
This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

class MainFragment : Fragment(R.layout.fragment_main), MainFragmentInterface {

    private val userDatabase by lazy { UserDatabase.getInstance(requireContext()).userDatabaseDao }
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: MainGridAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMainBinding.bind(view)

        adapter = MainGridAdapter(initFragmentObjects(), requireContext())
        binding.gridViewMain.adapter = adapter

        binding.gridViewMain.setOnItemClickListener { _, _, position, _->
            buttonNavigation(position)
        }

        binding.dataInFragment.text = getTodayMainPattern()

        val inputImage = ResourcesCompat.getDrawable(resources, R.drawable.ic_search_24, resources.newTheme())!!.apply {
            setBounds(60,0,0,0)
        }
        DrawableCompat.setTint(inputImage, ContextCompat.getColor(requireContext(), R.color.colorBlack))

        binding.searchInFragment.setCompoundDrawablesWithIntrinsicBounds(inputImage, null, null, null)
        binding.searchInFragment.compoundDrawablePadding = 18

        binding.searchInFragment.doOnTextChanged { text, _, _, _->
            val defaultList = initFragmentObjects().filter {
                it.values.first().lowercase(Locale.getDefault()).contains(text.toString()
                    .lowercase(Locale.getDefault()))
            }
            adapter.updateData(ArrayList(defaultList))

            binding.plugTextEmptyGrid.visibility = if (defaultList.isEmpty()) View.VISIBLE
            else View.GONE
        }

        lifecycleScope.launch {
            val user = getUser()
            setupAvatar(user.avatar)
        }

        MainActivity.mainState?.let {
            buttonNavigation(it)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTodayMainPattern(): String {
        val date = SimpleDateFormat("MM-dd").format(Date())
        val dateWeek = SimpleDateFormat("EEEE").format(Date())
        return "${date.split('-')[1]} " +
                "${date.split('-')[0].toInt().getMonth(StringCase.Genitive)}, "+
                dateWeek.replaceFirstChar { it.titlecase(Locale.getDefault())}
    }

    private fun initFragmentObjects(): ArrayList<HashMap<Any, String>> =
        ArrayList<HashMap<Any, String>>().apply {
            add(hashMapOf(R.drawable.ic_menu_gallery to "Галерея"))
            add(hashMapOf(R.drawable.ic_bugs to "Отчетность"))
            add(hashMapOf(R.drawable.ic_mic to "Диктофон"))
            add(hashMapOf(R.drawable.ic_mv to "Модальные окна"))
            add(hashMapOf(R.drawable.ic_upload to "Загрузка"))
            add(hashMapOf(R.drawable.ic_longread to "Лонгриды"))
            add(hashMapOf(R.drawable.ic_conf to "Инициализация"))
    }

    private suspend fun getUser(): UserData = withContext(Dispatchers.IO) {
        return@withContext userDatabase.getUserById(1)
    }

    override fun buttonNavigation(position: Int) {
        val navigateTo = when(position) {
            0 -> MainFragmentDirections.actionNavMainToNavSlideshow()
            1 -> MainFragmentDirections.actionNavMainToNavBugs()
            2 -> MainFragmentDirections.actionNavMainToNavDictophone()
            3 -> MainFragmentDirections.actionNavMainToNavMv()
            4 -> MainFragmentDirections.actionNavMainToNavUpload()
            5 -> MainFragmentDirections.actionNavMainToNavLongread()
            6 -> MainFragmentDirections.actionNavMainToNavConf()
            else -> MainFragmentDirections.actionNavMainToNavLongread()
        }
        MainActivity.mainState = position
        findNavController().navigate(navigateTo)
    }

    override fun setupAvatar(url: String) {
        Picasso.get().load(url).into(binding.userImageMain)
    }
}