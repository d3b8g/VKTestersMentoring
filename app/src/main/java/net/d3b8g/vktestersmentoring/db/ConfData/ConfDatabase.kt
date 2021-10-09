package net.d3b8g.vktestersmentoring.db.ConfData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.d3b8g.vktestersmentoring.db.UserData.UserData

@Database(entities = [UserData::class, ConfData::class], version = 1, exportSchema = false)
abstract class ConfDatabase : RoomDatabase() {

    abstract val confDatabaseDao: ConfDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ConfDatabase? = null

        fun getInstance(ct: Context): ConfDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        ct.applicationContext,
                        ConfDatabase::class.java,
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