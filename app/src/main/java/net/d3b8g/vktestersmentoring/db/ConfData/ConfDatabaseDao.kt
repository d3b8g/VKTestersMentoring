package net.d3b8g.vktestersmentoring.db.ConfData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ConfDatabaseDao {

    @Query("DELETE FROM confbase")
    fun deleteAll()

    @Insert
    fun insert(data: ConfData)

    @Update
    fun update(data: ConfData)

    @Query("SELECT * FROM confbase WHERE id == :id")
    fun getUserById(id: Int): ConfData
}