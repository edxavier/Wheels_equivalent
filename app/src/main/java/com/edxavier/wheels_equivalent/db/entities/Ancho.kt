package com.edxavier.wheels_equivalent.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anchos_table")
data class Ancho(
    @PrimaryKey(autoGenerate = false)
    val ancho: Int
){
    override fun toString(): String {
        return ancho.toString()
    }
}
