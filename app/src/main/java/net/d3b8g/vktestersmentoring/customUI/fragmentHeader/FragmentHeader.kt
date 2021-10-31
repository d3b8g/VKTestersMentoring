package net.d3b8g.vktestersmentoring.customUI.fragmentHeader

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import net.d3b8g.vktestersmentoring.R

/*
Copyright (c) 2021 github.com/d3b8g
All Rights Reserved
 
This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.

Use this code only for non commercial purpose.
*/

class FragmentHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle), FragmentHeaderInterface {

    private val titleView: TextView
    private val leftButton: ImageButton
    private val rightButton: ImageButton

    init {
        inflate(context, R.layout.fragment_header, this)
        orientation = VERTICAL

        titleView = findViewById(R.id.fragment_header_title)
        leftButton = findViewById(R.id.fragment_header_left_button)
        rightButton = findViewById(R.id.fragment_header_right_button)
    }

    override fun setTitleText(title: String) {
        titleView.text = title
    }

    override fun setRightButtonIcon(drawable: Drawable) {
        rightButton.visibility = View.VISIBLE
        rightButton.background = drawable
    }

    override fun setRightButtonListener(onClick: OnClickListener) {
        rightButton.setOnClickListener(onClick)
    }

    override fun setLeftButtonIcon(drawable: Drawable) {
        leftButton.visibility = View.VISIBLE
        leftButton.background = drawable
    }

    override fun setLeftButtonListener(onClick: OnClickListener) {
        leftButton.setOnClickListener(onClick)
    }


}