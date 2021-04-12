package net.d3b8g.vktestersmentoring.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.core.content.edit
import net.d3b8g.vktestersmentoring.modules.UserConfData

class ConfDataExist(ct: Context): SQLiteOpenHelper(ct, db_name, null, 1){

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE $table_name ("+
                "$col_uid INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "$col_ident VARCHAR(256), "+
                "$col_password VARCHAR(256)"

        db?.execSQL(createUserTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldV: Int, newV: Int) {}

    fun insertData(user: UserConfData, ct:Context){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(col_ident,user.login)
        cv.put(col_password,user.password)
        var result = db.insert(table_name, null, cv)
        if(result == (-1).toLong()) Toast.makeText(ct,"Некорректные данные", Toast.LENGTH_SHORT).show()
        else {
            PreferenceManager.getDefaultSharedPreferences(ct).edit {
                putInt("active_user_id", user.id).apply()
            }
        }
    }

    fun readUserData(id:Int): UserConfData? {
        val db = this.readableDatabase
        val query = "Select * from $table_name"
        val result = db.rawQuery(query, null)
        return if (result.moveToFirst()) {
            var data:UserConfData
            do{
                data = UserConfData(
                    id = result.getString(result.getColumnIndex(col_uid)).toInt(),
                    login = result.getString(result.getColumnIndex(col_ident)),
                    password = result.getString(result.getColumnIndex(col_password))
                )
            }
            while (result.moveToNext() && result.getString(result.getColumnIndex(CreateUserExist.col_uid)).toInt()!=id)
            return data
        } else null
    }

    companion object{
        val db_name = "VKTM.CONF"
        val table_name = "ConfData"
        val col_uid = "id"
        val col_ident = "ident"
        val col_password = "password"
    }

}