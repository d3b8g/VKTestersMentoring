package net.d3b8g.vktestersmentoring.ui.MV.popup

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import net.d3b8g.vktestersmentoring.R


class UploadPhoto(val ct: Context){

    fun show(){
        val frame = Dialog(ct)
        frame.setContentView(R.layout.alert_upload)
        frame.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.FILL_PARENT)
        frame.window!!.setGravity(Gravity.BOTTOM)

        frame.setCanceledOnTouchOutside(true)

        val og = frame.findViewById<Button>(R.id.au_open)
        og.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.type = "image/*"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(ct,intent,null)
        }

        frame.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        frame.show()
    }

}