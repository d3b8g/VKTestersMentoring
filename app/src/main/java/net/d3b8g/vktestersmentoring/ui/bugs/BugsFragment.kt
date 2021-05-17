package net.d3b8g.vktestersmentoring.ui.bugs

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.d3b8g.vktestersmentoring.R

class BugsFragment : Fragment() {

    lateinit var rep:TextInputEditText
    lateinit var rep_wanna:TextInputEditText
    lateinit var tN:TextView
    lateinit var tW:TextView
    lateinit var mT:TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_bugs, container, false)

        rep = root.findViewById(R.id.count_rep)
        rep_wanna = root.findViewById(R.id.count_rep_wanna)
        val result = root.findViewById<TextView>(R.id.percent_result)
        tN = root.findViewById(R.id.now_i_have)
        tW = root.findViewById(R.id.i_will_have)
        mT = root.findViewById(R.id.motivation_text)

        setupTextValue()
        rep.onFocusChangeListener = View.OnFocusChangeListener { view, hadFocus ->
            if (!hadFocus && !rep.text.toString().isNullOrBlank() && rep.text.toString().filter { it.isDigit() }!= "" ) {
                PreferenceManager.getDefaultSharedPreferences(root.context).edit {
                    putInt("report_count_now",rep.text.toString().filter { it.isDigit() }.toInt())
                }
                rep.clearFocus()
                result.text = getPercent()
                setupTextValue()
            }else if(!rep.text.toString().isNullOrBlank() && !rep.text!!.matches("\\d+".toRegex())){
                var er = root.findViewById<TextInputLayout>(R.id.l_count_rep)
                er.error = "Поле должно содержать цифры. Текст будет автоматически удален."
            }

        }
        rep_wanna.onFocusChangeListener = View.OnFocusChangeListener { view, hadFocus ->
            if (!hadFocus && !rep_wanna.text.toString().isNullOrBlank() && rep_wanna.text.toString().filter { it.isDigit() } != "" && rep_wanna.text.toString()!= "0" ) {
                PreferenceManager.getDefaultSharedPreferences(root.context).edit {
                    putInt("report_count_wanna", rep_wanna.text.toString().filter { it.isDigit() }.toInt())
                }
                rep_wanna.clearFocus()
                result.text = getPercent()
                setupTextValue()
            }else if(!rep_wanna.text.toString().isNullOrBlank() && !rep_wanna.text!!.matches("\\d+".toRegex())) {
                var er = root.findViewById<TextInputLayout>(R.id.l_wanna_rep)
                er.error = "Поле должно содержать цифры. Текст будет автоматически удален."
            }
        }

        rep_wanna.setOnEditorActionListener { it, i, keyEvent ->
            if(i ==  EditorInfo.IME_ACTION_DONE && rep_wanna.text.toString() != "0") {
                it.clearFocus()
                val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                PreferenceManager.getDefaultSharedPreferences(root.context).edit {
                    putInt("report_count_wanna",it.text.toString().filter { it.isDigit() }.toInt())
                }
                result.text = getPercent()
                setupTextValue()
                true
            }else{
                rep_wanna.setText("")
                var er = root.findViewById<TextInputLayout>(R.id.l_wanna_rep)
                er.error = "О каком росте идет речь, если желаешь 0 отчетов? "
            }
            false
        }

        result.text = getPercent()
        return root
    }

    private fun getPercent() = ((100 * PreferenceManager.getDefaultSharedPreferences(activity).getInt("report_count_now", 0)) /
            PreferenceManager.getDefaultSharedPreferences(activity).getInt("report_count_wanna", 1)).toString() + "%"

    private fun setupTextValue() {
        tN.text = "Сейчас у меня: ${PreferenceManager.getDefaultSharedPreferences(activity).getInt("report_count_now",0)} отчетов"
        tW.text = "Желаю зарепортить: ${PreferenceManager.getDefaultSharedPreferences(activity).getInt("report_count_wanna",1)}"
        mT.text = if(getPercent().dropLast(1).toInt() < 101) {
            getText(R.string.bad_scope)
        }else{
            getText(R.string.well_scope)
        }
    }
}