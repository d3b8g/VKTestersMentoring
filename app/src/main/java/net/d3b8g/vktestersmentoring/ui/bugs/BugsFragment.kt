package net.d3b8g.vktestersmentoring.ui.bugs

import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import net.d3b8g.vktestersmentoring.R

class BugsFragment : Fragment() {

    lateinit var rep:TextInputEditText
    lateinit var rep_wanna:TextInputEditText
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_bugs, container, false)

        rep = root.findViewById(R.id.count_rep)
        rep_wanna = root.findViewById(R.id.count_rep_wanna)
        val result = root.findViewById<TextView>(R.id.percent_result)

        rep.onFocusChangeListener = View.OnFocusChangeListener { view, hadFocus ->
            if (!hadFocus && !rep.text.toString().isNullOrBlank()) {
                //rep.text.toString().filter { it.isDigit() }.toInt()
                PreferenceManager.getDefaultSharedPreferences(root.context).edit {
                    putInt("report_count_now",rep.text.toString().toInt())
                }
                rep.clearFocus()
                result.text = "${getPercent()}%"
            }
        }
        rep_wanna.onFocusChangeListener = View.OnFocusChangeListener { view, hadFocus ->
            if (!hadFocus && !rep_wanna.text.toString().isNullOrBlank()) {
                //rep.text.toString().filter { it.isDigit() }.toInt()
                PreferenceManager.getDefaultSharedPreferences(root.context).edit {
                    putInt("report_count_wanna",rep_wanna.text.toString().toInt())
                }
                rep_wanna.clearFocus()
                result.text = "${getPercent()}%"
            }
        }

        result.text = "${getPercent()}%"
        return root
    }
    fun getPercent() = (100*PreferenceManager.getDefaultSharedPreferences(activity).getInt("report_count_now",0))/PreferenceManager.getDefaultSharedPreferences(activity).getInt("report_count_wanna",1)
}