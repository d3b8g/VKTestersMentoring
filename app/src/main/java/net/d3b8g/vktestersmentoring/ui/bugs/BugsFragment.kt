package net.d3b8g.vktestersmentoring.ui.bugs

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.customUI.fragmentHeader.FragmentHeader
import net.d3b8g.vktestersmentoring.databinding.FragmentBugsBinding

class BugsFragment : Fragment(R.layout.fragment_bugs) {

    private lateinit var binding: FragmentBugsBinding
    private val fragmentHeader: FragmentHeader by lazy {
        binding.bugsHeader
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        binding = FragmentBugsBinding.bind(root)

        setupTextValue()
        binding.countRep.onFocusChangeListener = View.OnFocusChangeListener { _, hadFocus ->
            if (!hadFocus && binding.countRep.text.toString().fieldChecker() ) {
                PreferenceManager.getDefaultSharedPreferences(root.context).edit {
                    putInt("report_count_now", binding.countRep.text.toString().filter { it.isDigit() }.toInt())
                }
                binding.countRep.clearFocus()
                binding.percentResult.text = getPercent()
                setupTextValue()
            } else if(binding.countRep.text.toString().isNotBlank() && !binding.countRep.text!!.matches("\\d+".toRegex())) {
                binding.lCountRep.error = "Поле должно содержать цифры. Текст будет автоматически удален."
            }

        }
        binding.countRepWanna.onFocusChangeListener = View.OnFocusChangeListener { view, hadFocus ->
            if (!hadFocus && binding.countRepWanna.text.toString().fieldChecker()) {
                    PreferenceManager.getDefaultSharedPreferences(root.context).edit {
                        putInt("report_count_wanna", binding.countRepWanna.text.toString().filter { it.isDigit() }.toInt())
                    }
                    binding.countRepWanna.clearFocus()
                    binding.percentResult.text = getPercent()
                    setupTextValue()
            } else if(binding.countRepWanna.text.toString().isNotBlank() && !binding.countRepWanna.text!!.matches("\\d+".toRegex())) {
                binding.lWannaRep.error = "Поле должно содержать цифры. Текст будет автоматически удален."
            }
        }

        binding.countRepWanna.setOnEditorActionListener { it, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE && binding.countRepWanna.text.toString().take(0) != "0") {
                it.clearFocus()
                val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                PreferenceManager.getDefaultSharedPreferences(root.context).edit {
                    if (it.text.toString().fieldChecker()) {
                        putInt("report_count_wanna", it.text.toString().filter { it.isDigit() }.toInt())
                    }
                }
                binding.percentResult.text = getPercent()
                setupTextValue()
            } else {
                binding.countRepWanna.setText("")
                binding.lWannaRep.error = "О каком росте идет речь, если желаешь 0 отчетов? "
            }
            false
        }
        binding.percentResult.text = getPercent()

        fragmentHeader.setTitleText("Отчетность")
        fragmentHeader.setRightButtonIcon(
            ResourcesCompat.getDrawable(resources ,R.drawable.ic_close, resources.newTheme())!!
        )
        fragmentHeader.setRightButtonListener {
            findNavController().popBackStack()
        }
    }

    private fun String.fieldChecker(): Boolean {
        return this.filter { it.isDigit() } != "" && this.take(1) != "0"
    }

    private fun getPercent() = ((100 * PreferenceManager.getDefaultSharedPreferences(activity).getInt("report_count_now", 0)) /
            PreferenceManager.getDefaultSharedPreferences(activity).getInt("report_count_wanna", 1)).toString() + "%"

    @SuppressLint("SetTextI18n")
    private fun setupTextValue() {
        binding.nowIHave.text = "Сейчас у меня: ${PreferenceManager.getDefaultSharedPreferences(activity).getInt("report_count_now",0)} отчетов"
        binding.iWillHave.text = "Желаю зарепортить: ${PreferenceManager.getDefaultSharedPreferences(activity).getInt("report_count_wanna",1)}"
        binding.motivationText.text = if(getPercent().dropLast(1).toInt() < 101) {
            getText(R.string.bad_scope)
        } else {
            getText(R.string.well_scope)
        }
    }
}