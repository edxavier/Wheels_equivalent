package com.edxavier.wheels_equivalent.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.edxavier.wheels_equivalent.db.entities.*
import com.edxavier.wheels_equivalent.db.wheelsDao.WheelsDao


@Database(
    entities = [Ancho::class, Perfil::class, Rin::class, Carga::class, Velocidad::class],
    version = 1)
internal abstract class WheelsDB:RoomDatabase(){
    abstract fun wheelsDao(): WheelsDao

    companion object{
        @Volatile
        private var instance:WheelsDB? = null

        fun getInstance(context:Context)= instance ?: synchronized(this){
            instance?: buildDataBase(context).also { instance = it }
        }

        private fun buildDataBase(context: Context): WheelsDB {
            return Room.databaseBuilder(
                context.applicationContext,
                WheelsDB::class.java,
                "wheels_db.db"
            ).fallbackToDestructiveMigration()
                .setJournalMode(JournalMode.AUTOMATIC)
                .build()
        }
    }

}