package com.edxavier.wheels_equivalent.db

import com.edxavier.wheels_equivalent.db.entities.*
import kotlin.math.roundToInt

class WheelData(
    var width: Ancho = Ancho(0),
    var profile: Perfil = Perfil(0),
    var rim: Rin = Rin(0),
    var speed: Velocidad = Velocidad("", "", 0),
    var load: Carga = Carga(0,""),

    var percDiff: Float = 0f,
    var speedDiff: Float = 0f,
    var isEquivalent: Boolean = false
){
    override fun toString(): String {
        val l = if(load.id_carga==0) "" else load.toString()
        return "${width}/$profile R$rim $l $speed"
    }

    fun getDiameter(): Float{
        return (width.ancho.toFloat() * (profile.perfil.toFloat() / 100) * 2 + rim.rin.toFloat() * 25.4).roundToInt().toFloat()
    }
    fun compareWheel(newWheel: WheelData): WheelData{
        val stdTotalDiameter = getDiameter()
        val newTotalDiameter = newWheel.getDiameter()
        val perc = (newTotalDiameter / stdTotalDiameter - 1) * 100
        newWheel.isEquivalent = (perc in -3.0..3.0)
        newWheel.percDiff = perc
        /*
            W = vel/diametro (rad/s)
            rpm = 60*W/2pi
            V = W * diametro (m/s)
            si  tomamos como base 100 kph y trbajamos con medidas del sistema internacional,
            habra de pasar la velociadad a m/s y el diametro a metros.
            ahora supongamos que la rueda original va a 100kph y queremos averiguar las rpm y asi
            averiguar la velocidad de la nueva rueda a esa misma rpm hacemos:
        */
        val vel = (100.toFloat() * 1000.toFloat() / 3600.toFloat()).toDouble() // m/s
        val w = vel / (stdTotalDiameter/1000)// rad/seg
        // val rpm = 60 * w / (Math.PI * 2) //rpm

        //teniendo las rpm del neumatico original a 100kph calculamos la velociada del nuevo a esas mismas rpm
        val velocidad = w * (newTotalDiameter/1000) * 3600 / 1000 //kmh
        newWheel.speedDiff = velocidad.toFloat()
        return newWheel
    }
}