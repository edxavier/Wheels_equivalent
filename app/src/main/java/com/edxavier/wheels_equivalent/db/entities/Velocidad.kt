package com.edxavier.wheels_equivalent.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "velocidades_table")
data class Velocidad(
    @PrimaryKey(autoGenerate = false)
    val id_velocidad: String,
    val valor: String,
    val index: Int
){
    override fun toString(): String {
        return id_velocidad
    }
}
