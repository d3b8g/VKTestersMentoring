package net.d3b8g.vktestersmentoring.db.UserData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.d3b8g.vktestersmentoring.db.ConfData.ConfData

@Database(entities = [UserData::class, ConfData::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract val userDatabaseDao: UserDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(ct: Context): UserDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        ct.applicationContext,
                        UserDatabase::class.java,
                        "userbase"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}