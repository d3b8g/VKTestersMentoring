package net.d3b8g.vktestersmentoring.customUI.fragmentHeader

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import net.d3b8g.vktestersmentoring.MainActivity
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.helper.ToolsShit.isDevicesDarkTheme

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
) : LinearLayout(context, attrs, defStyle) {

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

    fun setTitleText(title: String) {
        if (context.isDevicesDarkTheme())
            titleView.setTextColor(resources.getColor(R.color.colorWhite, resources.newTheme()))
        titleView.text = title
    }

    fun setRightButtonIcon(drawable: Drawable, onClick: OnClickListener) {
        if (context.isDevicesDarkTheme())
            rightButton.backgroundTintList = AppCompatResources.getColorStateList(context, R.color.colorBlueLight)

        rightButton.visibility = View.VISIBLE
        rightButton.background = drawable

        rightButton.setOnClickListener(onClick)
        MainActivity.mainState = null
    }

    fun setLeftButtonIcon(drawable: Drawable, onClick: OnClickListener) {
        leftButton.visibility = View.VISIBLE
        leftButton.background = drawable
        leftButton.setOnClickListener(onClick)
    }
}