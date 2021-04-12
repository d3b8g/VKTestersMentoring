package net.d3b8g.vktestersmentoring.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import net.d3b8g.vktestersmentoring.modules.UserData



class CreateUserExist(ct:Context): SQLiteOpenHelper(ct, db_name, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE $table_name ("+
                "$col_uid INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "$col_username VARCHAR(256), "+
                "$col_avatar VARCHAR(256), "+
                "$col_scope INTEGER, "+
                "$col_counter INTEGER)"

        db?.execSQL(createUserTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldV: Int, newV: Int) {

    }

    fun insertData(user:UserData,ct:Context){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(col_username,user.username)
        cv.put(col_avatar,user.avatar)
        cv.put(col_scope,user.scope)
        cv.put(col_counter,user.counter)
        var result = db.insert(table_name, null, cv)
        if(result == (-1).toLong()) Toast.makeText(ct,"Некорректные данные",Toast.LENGTH_SHORT).show()
        else {
            PreferenceManager.getDefaultSharedPreferences(ct).edit {
                putInt("active_user_id", user.id).apply()
            }
        }
    }

    fun readUserData(id:Int): UserData? {
        val db = this.readableDatabase
        val query = "Select * from $table_name"
        val result = db.rawQuery(query, null)
        return if (result.moveToFirst()) {
            var data:UserData
            do{
                data = UserData(
                    id = result.getString(result.getColumnIndex(col_uid)).toInt(),
                    username = result.getString(result.getColumnIndex(col_username)),
                    avatar = result.getString(result.getColumnIndex(col_avatar)),
                    scope = result.getString(result.getColumnIndex(col_scope)).toInt(),
                    counter = result.getString(result.getColumnIndex(col_counter)).toInt()
                )
            }
            while (result.moveToNext() && result.getString(result.getColumnIndex(col_uid)).toInt()!=id)
            return data
        } else null
    }


    companion object{
        val db_name = "VKTM.USERS"
        val table_name = "Users"
        val col_uid = "id"
        val col_username = "username"
        val col_avatar = "avatar"
        val col_scope = "scope"
        val col_counter = "counter"
    }

    //var db = DataBaseHandler(context) .... db.insertData  

}