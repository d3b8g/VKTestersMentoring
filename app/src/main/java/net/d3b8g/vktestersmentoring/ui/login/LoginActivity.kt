package net.d3b8g.vktestersmentoring.ui.login

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*
import net.d3b8g.vktestersmentoring.MainActivity
import net.d3b8g.vktestersmentoring.MainActivity.Companion.uid
import net.d3b8g.vktestersmentoring.R
import net.d3b8g.vktestersmentoring.adapters.UserAdapter
import net.d3b8g.vktestersmentoring.db.CreateUserExist
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_avatar
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_counter
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_scope
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_uid
import net.d3b8g.vktestersmentoring.db.CreateUserExist.Companion.col_username
import net.d3b8g.vktestersmentoring.interfaces.Login
import net.d3b8g.vktestersmentoring.modules.UserConfData
import net.d3b8g.vktestersmentoring.modules.UserData

class LoginActivity : AppCompatActivity(), Login {

    lateinit var input: TextInputEditText
    lateinit var rotatedLinear: LinearLayout
    lateinit var rcv: RecyclerView
    lateinit var loginText: TextView
    lateinit var tlPass: TextInputLayout

    lateinit var adapter:UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            if(getBoolean("make_splash", false)){
                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                finish()
            }
        }

        setContentView(R.layout.activity_login)

        val btn = findViewById<Button>(R.id.register_start)
        input = findViewById(R.id.register_input)
        rotatedLinear = findViewById(R.id.rotated_layout)
        rcv = findViewById(R.id.user_db)
        loginText = findViewById(R.id.login_text)
        tlPass = findViewById(R.id.tl_input_password)

        adapter = UserAdapter(this)
        rcv.adapter = adapter
        rcv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rcv.setHasFixedSize(true)
        updateUserData()
        btn.setOnClickListener {
            input.let {
                if(!it.text.isNullOrEmpty() &&
                    it.text!!.length>3 &&
                    it.text!!.contains(' ') &&
                    it.text!!.any { text -> text.isLetter() }) {

                    var user_id:Int
                    PreferenceManager.getDefaultSharedPreferences(this).apply {
                        user_id = getInt("active_user_id", 0) + 1
                    }
                    PreferenceManager.getDefaultSharedPreferences(this).edit {
                        putBoolean("make_splash", true).apply()
                        putInt("active_user_id",user_id)
                    }
                    CreateUserExist(this@LoginActivity).insertData(
                        UserData(
                            id = 0,
                            username = it.text!!.toString(),
                            avatar = "https://sun9-67.userapi.com/impg/I8qae64Ppm2JRUm4E_ioXR7rgSpfLY81K02nUg/XN3Fn9zsP5g.jpg?size=726x612&quality=96&proxy=1&sign=6412c55a2d2a6b5c06c31ca6de71aab1",
                            scope = 0,
                            counter = 0
                        ),
                        UserConfData(
                            id = 0,
                            login = "null@vktm",
                            password = ""
                        ), this@LoginActivity)
                    startActivity(Intent(this,MainActivity::class.java))
                }else{
                    Toast.makeText(this, "Поле должно содержать Имя и Фамилию.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateUserData() {
        val listBack:ArrayList<UserData> = ArrayList()
        val db = CreateUserExist(this).readableDatabase
        val query = "Select * from ${CreateUserExist.table_name[0]}"
        try {
            val result = db.rawQuery(query, null)
            if (result.moveToFirst()) {
                do{
                    listBack.add(UserData(
                        id = result.getString(result.getColumnIndex(col_uid)).toInt(),
                        username = result.getString(result.getColumnIndex(col_username)),
                        avatar = result.getString(result.getColumnIndex(col_avatar)),
                        scope = result.getString(result.getColumnIndex(col_scope)).toInt(),
                        counter = result.getString(result.getColumnIndex(col_counter)).toInt()
                    ))
                }while (result.moveToNext())
            }
        }catch (e: Exception) {
            e.stackTrace
        }

        if(!listBack.none()){
            rcv.visibility = View.VISIBLE
            adapter.setUser(listBack)
            loginText.text = getString(R.string.login)
        }
    }

    override fun loginUser(id: Int) {
        val pass = CreateUserExist(this).readConfData(id)!!.password
        if(pass.isNotEmpty()) {
            tlPass.visibility = View.VISIBLE
            login.visibility = View.VISIBLE
            login.setOnClickListener {
                login_password.let {
                    if(!it.text.isNullOrEmpty() && it.text.toString() == pass) {
                        PreferenceManager.getDefaultSharedPreferences(this).edit {
                            putBoolean("make_splash", true).apply()
                            putInt("active_user_id",id)
                        }
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }else{
                        tlPass.error = getString(R.string.wrong_password)
                    }
                }
            }
        } else {
            PreferenceManager.getDefaultSharedPreferences(this).edit {
                putBoolean("make_splash", true).apply()
                putInt("active_user_id",id)
            }
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        if(uid == 1) super.onBackPressed()
        else Toast.makeText(this, "Необходимо авторизоваться", Toast.LENGTH_SHORT).show()
    }

}