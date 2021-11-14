package net.d3b8g.vktestersmentoring.ui.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import net.d3b8g.vktestersmentoring.R


/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved
 
This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

class MainGridAdapter(private var listOfFragments: ArrayList<HashMap<Any, String>>, val ct: Context)
    : BaseAdapter() {

    fun updateData(newList: ArrayList<HashMap<Any, String>>) {
        listOfFragments.clear()
        listOfFragments.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = listOfFragments.size

    override fun getItem(position: Int): Any = listOfFragments[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v: View? = convertView

        if (v == null) {
            val bb = ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = bb.inflate(R.layout.cell_navigation_in_fragment, parent, false)!!
        }

        val title = v.findViewById<TextView>(R.id.titleInGrid)
        val image = v.findViewById<ImageView>(R.id.imageInGrid)

        val keyInImage = listOfFragments[position].keys.first() as Int
        val picture = ct.resources.getDrawable(keyInImage, ct.theme) as Drawable

        title.text = listOfFragments[position].values.first()
        image.setImageDrawable(picture)
        //image.setColorFilter(ContextCompat.getColor(ct, R.color.backgroundImageTint))
//        image.setBackgroundResource(R.drawable.alert_shape)
//        image.background.alpha = 75

        return v
    }

}