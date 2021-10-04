package net.d3b8g.vktestersmentoring.db.UserData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userbase")
data class UserData (

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "avatar")
    val avatar: String,

    @ColumnInfo(name = "scope")
    val scope: Int,

    @ColumnInfo(name = "counter")
    val counter: Int,
)