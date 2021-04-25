package net.d3b8g.vktestersmentoring.ui.confdata

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.d3b8g.vktestersmentoring.MainActivity.Companion.uid
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.db.CreateUserExist
import net.d3b8g.vktestersmentoring.modules.UserConfData

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

        CreateUserExist(root.context).readConfData(uid)?.login?.apply {
            if (this != "null@vktm"){
                ident.text = this
                tlIdent.visibility = View.GONE
                tlPass.visibility = View.GONE
                save.isClickable = false
            }
        }

        save.setOnClickListener {
            genPass(root.context)
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
/*    NOT ACTUAL METHOD
    private fun genPass():String {
        val md = MessageDigest.getInstance("MD5")
        val code = BigInteger(1, md.digest(setIdent.text.toString().toByteArray())).toString(16).padStart(32, '0')
        val randomDigit = (10..code.length-2).random()
        val getCharReplaced = "${code[randomDigit]}${code[randomDigit+1]}${code[randomDigit+2]}"
        return code.replaceFirst(getCharReplaced,setPass.text!!.length.toString()+setPass.text)+randomDigit
    }
*/

    private fun genPass(ct:Context):String{
        CreateUserExist(ct).readUserData(uid)!!.username.apply {
            var username = ""
            this.split(' ').forEachIndexed { index,un ->
                username += when(index){
                    0-> un
                    1-> setIdent.text.toString().replace("@vktm","") + un
                    else -> null
                }
            }
            val str = Base64.encodeToString(username.reversed().toByteArray(),Base64.DEFAULT).replace("=","").reversed()
            val randomKey = (str.indices-1).random()
            val std = Base64.encodeToString((str.replace("${str[randomKey]}${str[randomKey+1]}",setPass.text.toString()) + randomKey).toByteArray(), Base64.DEFAULT).replace("=","")
            return (std.substring(0,(std.length-1)/2).reversed() + std.substring((std.length-1)/2+1,std.length-1)).reversed()
        }
    }

    private fun saveUserConf(ct:Context) {
        try {
            val updateUserConf = CreateUserExist(ct).writeConfData(UserConfData(
                id = uid,
                login = setIdent.text.toString(),
                password = genPass(ct)
            ))

            if(updateUserConf){
                ident.text = setIdent.text.toString()
                tlIdent.visibility = View.GONE
                tlPass.visibility = View.GONE
                save.isClickable = false
                Toast.makeText(ct,"Данные обновлены",Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            e.stackTrace
        }
    }
}