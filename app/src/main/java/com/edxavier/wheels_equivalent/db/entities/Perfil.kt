package com.edxavier.wheels_equivalent.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfiles_table")
data class Perfil(
    @PrimaryKey(autoGenerate = false)
    val perfil: Int
){
    override fun toString(): String {
        return perfil.toString()
    }
}
