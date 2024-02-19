package com.edxavier.wheels_equivalent.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rines_table")
data class Rin(
    @PrimaryKey(autoGenerate = false)
    val rin: Int
){
    override fun toString(): String {
        return rin.toString()
    }
}
