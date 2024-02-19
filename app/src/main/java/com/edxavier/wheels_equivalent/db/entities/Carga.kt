package com.edxavier.wheels_equivalent.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cargas_table")
data class Carga(
    @PrimaryKey(autoGenerate = false)
    val id_carga: Int,
    val valor: String
){
    override fun toString(): String {
        return id_carga.toString()
    }
}
