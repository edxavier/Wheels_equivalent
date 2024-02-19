package com.edxavier.wheels_equivalent.db

import com.edxavier.wheels_equivalent.db.entities.*

class WheelDataDto (
    var widths: List<Ancho> = listOf(),
    var profiles: List<Perfil> = listOf(),
    var rims: List<Rin> = listOf(),
    var speeds: List<Velocidad> = listOf(),
    var loads: List<Carga> = listOf()
)