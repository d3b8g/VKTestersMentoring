package net.d3b8g.vktestersmentoring.ui.splashscreen

import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.android.material.textfield.TextInputEditText
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.prefs.pMineName

class SplashScreen:AppCompatActivity() {

    lateinit var input:TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_splash)

        val btn = findViewById<Button>(R.id.register_start)
        input = findViewById(R.id.register_input)

        btn.setOnClickListener {
            input?.let {it->
                if(!it.text.isNullOrEmpty() && it.text!!.length>3 && it.text!!.contains(' ')){
                    pMineName(this,true,it.text!!.toString())
                    PreferenceManager.getDefaultSharedPreferences(this).edit {
                        putBoolean("make_splash",true).apply()
                    }
                    finish()
                }else{
                    Toast.makeText(this,"Поле должно содержать Имя и Фамилию.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(!input.text!!.isNullOrEmpty()){
            PreferenceManager.getDefaultSharedPreferences(this).edit {
                putBoolean("make_splash",true).apply()
            }
            finish()
        }else{
            Toast.makeText(this,"Вы не заполнили поле", Toast.LENGTH_SHORT).show()
        }
    }
}