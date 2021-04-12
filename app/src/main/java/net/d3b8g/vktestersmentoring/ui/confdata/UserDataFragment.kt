package net.d3b8g.vktestersmentoring.ui.confdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.d3b8g.vktestersmentoring.R

class UserDataFragment : Fragment() {

    lateinit var ident:TextView
    lateinit var setIdent:TextInputEditText
    lateinit var setPassword:TextInputEditText
    lateinit var tlIdent:TextInputLayout
    lateinit var tlPassword:TextInputLayout
    lateinit var save:Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_userdata,container,false)

        ident = root.findViewById(R.id.conf_uident)
        setIdent = root.findViewById(R.id.conf_ident)
        tlIdent = root.findViewById(R.id.conf_ident_tl)
        tlPassword = root.findViewById(R.id.conf_password_tl)

        setPassword = root.findViewById(R.id.conf_password)
        save = root.findViewById(R.id.conf_save)

        save.setOnClickListener {
            if(setIdent.text.toString().length > 5){

            }else{
                tlIdent.error = "Формат записи: ID@vktm"
            }
            if(setPassword.text.toString().length > 8){

            }else{
                tlPassword.error = "Пароль не может быть меньше 8 символов"
            }
        }

        return root
    }
}