package com.edxavier.wheels_equivalent.db.wheelsDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.edxavier.wheels_equivalent.db.entities.*

@Dao
interface WheelsDao {
    @Query("SELECT * FROM anchos_table")
    fun getWidths(): List<Ancho>

    @Query("SELECT * FROM perfiles_table")
    fun getProfiles(): List<Perfil>

    @Query("SELECT * FROM rines_table")
    fun getRims(): List<Rin>

    @Query("SELECT * FROM cargas_table")
    fun getLoads(): List<Carga>

    @Query("SELECT * FROM velocidades_table")
    fun getSpeeds(): List<Velocidad>

    // SAVES
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWidth(width: Ancho)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProfile(profile: Perfil)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRim(rim: Rin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLoad(load: Carga)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSpeed(speed: Velocidad)

}