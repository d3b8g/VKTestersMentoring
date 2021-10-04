package net.d3b8g.vktestersmentoring.db.ConfData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "confbase")
data class ConfData (

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "ident")
    val ident: String,

    @ColumnInfo(name = "password")
    val password: String
)