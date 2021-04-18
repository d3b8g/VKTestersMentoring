package net.d3b8g.vktestersmentoring.ui.confdata

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.d3b8g.vktestersmentoring.MainActivity
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.db.CreateUserExist
import java.math.BigInteger
import java.security.MessageDigest

class UserDataFragment : Fragment() {

    lateinit var ident:TextView
    lateinit var setIdent:TextInputEditText
    lateinit var tlIdent:TextInputLayout

    lateinit var setPass:TextInputEditText
    lateinit var tlPass:TextInputLayout

    lateinit var save:Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_userdata,container,false)

        ident = root.findViewById(R.id.conf_uident)
        setIdent = root.findViewById(R.id.conf_ident)
        tlIdent = root.findViewById(R.id.conf_ident_tl)

        setPass = root.findViewById(R.id.conf_password)
        tlPass = root.findViewById(R.id.conf_password_tl)

        save = root.findViewById(R.id.conf_save)

        ident.text = CreateUserExist(root.context).readConfData(MainActivity.uid)?.login

        save.setOnClickListener {
            if(setPass.text.toString().length in 2..4){
                if(setIdent.text.toString().length > 5 &&
                    setIdent.text.toString().contains("@vktm") &&
                    setIdent.text.toString().split("@")[0].matches("^\\d+\$".toRegex())){
                    saveUserConf(root.context)
                }else{
                    tlIdent.error = "Формат записи: ID@vktm"
                }
            }else{
                tlPass.error = "Допустимо от 2 до 4 символов"
            }
        }

        return root
    }

    private fun genPass():String {
        val md = MessageDigest.getInstance("MD5")
        val code =  BigInteger(1, md.digest(setIdent.text.toString().toByteArray())).toString(16).padStart(32, '0')
        val randomDigit = (10..code.length-2).random()
        val getCharReplaced = "${code[randomDigit]}${code[randomDigit+1]}${code[randomDigit+2]}"
        return code.replaceFirst(getCharReplaced,setPass.text.toString())+randomDigit
    }

    private fun saveUserConf(ct:Context) {
        PreferenceManager.getDefaultSharedPreferences(activity).apply {
            try {
            }catch (e:Exception){
                e.stackTrace
            }
        }
    }
}