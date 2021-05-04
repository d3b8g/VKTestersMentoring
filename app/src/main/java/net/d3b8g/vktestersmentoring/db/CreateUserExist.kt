package net.d3b8g.vktestersmentoring.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import net.d3b8g.vktestersmentoring.modules.UserConfData
import net.d3b8g.vktestersmentoring.modules.UserData


class CreateUserExist(ct:Context): SQLiteOpenHelper(ct, db_name, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE ${table_name[0]} ("+
                "$col_uid INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "$col_username VARCHAR(256), "+
                "$col_avatar VARCHAR(256), "+
                "$col_scope INTEGER, "+
                "$col_counter INTEGER)"

        val createConfTable = "CREATE TABLE ${table_name[1]} ("+
                "$col_uid INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "$col_ident VARCHAR(256), "+
                "$col_password VARCHAR(256))"

        db?.execSQL(createUserTable)
        db?.execSQL(createConfTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldV: Int, newV: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${table_name[0]}")
        db?.execSQL("DROP TABLE IF EXISTS ${table_name[1]}")
        onCreate(db)
    }

    fun insertData(user:UserData,conf:UserConfData,ct:Context) {
        val db = this.writableDatabase
        val ucv = ContentValues().apply {
            put(col_username,user.username)
            put(col_avatar,user.avatar)
            put(col_scope,user.scope)
            put(col_counter,user.counter)
        }
        val ccv = ContentValues().apply {
            put(col_ident,conf.login)
            put(col_password,conf.password)
        }

        db.insert(table_name[0],null,ucv).apply {
            if(this == (-1).toLong()) Toast.makeText(ct,"Некорректные данные",Toast.LENGTH_SHORT).show()
        }
        db.insert(table_name[1],null,ccv).apply {
            if(this == (-1).toLong()) Toast.makeText(ct,"Некорректные данные (2)",Toast.LENGTH_SHORT).show()
        }
    }

    fun readUserData(id:Int): UserData? {
        val db = this.readableDatabase
        val query = "Select * from ${table_name[0]}"
        val result = db.rawQuery(query,null)
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
            db.close()
            return data
        } else null
    }

    fun readConfData(id:Int):UserConfData? {
        val db = this.readableDatabase
        val query = "Select * from ${table_name[1]}"
        val result = db.rawQuery(query,null)
        return if (result.moveToFirst()) {
            var data:UserConfData
            do{
                data = UserConfData(
                    id = result.getString(result.getColumnIndex(col_uid)).toInt(),
                    login = result.getString(result.getColumnIndex(col_ident)),
                    password = result.getString(result.getColumnIndex(col_password))
                )
            }
            while (result.moveToNext() && result.getString(result.getColumnIndex(col_uid)).toInt()!=id)
            db.close()
            return data
        } else null
    }

    fun writeConfData(user:UserConfData): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(col_ident,user.login)
            put(col_password,user.password)
        }
        val comp = db.update(table_name[1],cv,"id = ?", arrayOf(user.id.toString()))
        return comp == 1
    }

    fun updateCountVisits(id:String, visits:Int): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(col_counter, visits)
        }
        val comp = db.update(table_name[0],cv,"id = ?", arrayOf(id))
        return comp == 1
    }

    companion object {
        const val db_name = "VKTM"
        val table_name = listOf("Users","ConfData")
        const val col_uid = "id"
        //Users
        const val col_username = "username"
        const val col_avatar = "avatar"
        const val col_scope = "scope"
        const val col_counter = "counter"
        //ConfData
        const val col_ident = "ident"
        const val col_password = "password"
    }
}