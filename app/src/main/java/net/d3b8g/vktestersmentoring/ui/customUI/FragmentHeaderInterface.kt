package net.d3b8g.vktestersmentoring.ui.customUI

import android.graphics.drawable.Drawable
import android.view.View

interface FragmentHeaderInterface {

    fun setTitleText(title: String)

    fun setRightButtonIcon(drawable: Drawable)
    fun setRightButtonListener(onClick: View.OnClickListener)

    fun setLeftButtonIcon(drawable: Drawable)
    fun setLeftButtonListener(onClick: View.OnClickListener)
}