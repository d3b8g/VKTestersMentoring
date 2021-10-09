package net.d3b8g.vktestersmentoring.db.UserData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDatabaseDao {

    @Query("DELETE FROM userbase")
    fun deleteAll()

    @Insert
    fun insert(data: UserData): Long

    @Update
    fun update(data: UserData)

    @Query("UPDATE userbase SET counter = :visits")
    fun updateVisitsCounter(visits: Int)

    @Query("SELECT * FROM userbase WHERE id == :id")
    fun getUserById(id: Int): UserData

    @Query("SELECT * FROM userbase")
    fun getAllData(): List<UserData>
}