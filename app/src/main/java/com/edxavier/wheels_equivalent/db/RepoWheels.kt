package com.edxavier.wheels_equivalent.db

import android.content.Context
import android.util.Log
import com.edxavier.wheels_equivalent.db.entities.*
import com.edxavier.wheels_equivalent.db.wheelsDao.WheelsDao

class RepoWheels(private val dao:WheelsDao) {
    fun getWidths(): List<Ancho> = dao.getWidths()
    fun getProfiles(): List<Perfil> = dao.getProfiles()
    fun getRims(): List<Rin> = dao.getRims()
    fun getLoads(): List<Carga> = dao.getLoads()
    fun getSpeeds(): List<Velocidad> = dao.getSpeeds()

    fun getSuggestions(wheel: WheelData): List<WheelData>{
        val wheels: ArrayList<WheelData> = ArrayList()
        val widths = getWidths()
        val profiles = getProfiles()
        for(rim in wheel.rim.rin-1..wheel.rim.rin+3){
            for (w in widths.filter { (wheel.width.ancho - it.ancho) in -30..30 }){
                for (p in profiles){
                    val sWheel = WheelData(w, p, Rin(rim),
                        Velocidad("","",0),
                        Carga(0, "")
                    )
                    val res = wheel.compareWheel(sWheel)
                    if(res.isEquivalent){
                        wheels.add(res)
                    }
                }
            }
        }
        return wheels
    }

    companion object{
        fun initWidths(context: Context){
            val dao = WheelsDB.getInstance(context).wheelsDao()
            if(dao.getWidths().isEmpty()){
                for (i in 135..355 step 5){
                    dao.saveWidth(Ancho(i))
                }
            }
        }
        fun initProfiles(context: Context){
            val dao = WheelsDB.getInstance(context).wheelsDao()
            if(dao.getProfiles().isEmpty()){
                dao.saveProfile(Perfil(15))
                dao.saveProfile(Perfil(81))
                dao.saveProfile(Perfil(82))
                for (i in 25..85 step 5){
                    dao.saveProfile(Perfil(i))
                }
            }
        }
        fun initRims(context: Context){
            val dao = WheelsDB.getInstance(context).wheelsDao()
            if(dao.getRims().isEmpty()){
                for (i in 10..23){
                    dao.saveRim(Rin(i))
                }
            }
        }
        fun initSpeeds(context: Context){
            val dao = WheelsDB.getInstance(context).wheelsDao()
            if(dao.getSpeeds().isEmpty()){
                val codigos = arrayOf(
                    "B",
                    "C",
                    "D",
                    "E",
                    "F",
                    "G",
                    "J",
                    "K",
                    "L",
                    "M",
                    "N",
                    "P",
                    "Q",
                    "R",
                    "S",
                    "T",
                    "U",
                    "H",
                    "V",
                    "ZR",
                    "W",
                    "Y"
                )
                val valores = arrayOf(
                    "50",
                    "60",
                    "65",
                    "70",
                    "80",
                    "90",
                    "100",
                    "110",
                    "120",
                    "130",
                    "140",
                    "150",
                    "160",
                    "170",
                    "180",
                    "190",
                    "200",
                    "210",
                    "240",
                    ">240",
                    "270",
                    "300"
                )
                for (i in codigos.indices){
                    dao.saveSpeed(Velocidad(index = i, valor = valores[i], id_velocidad = codigos[i]))
                }

            }
        }
        fun initLoads(context: Context){
            val dao = WheelsDB.getInstance(context).wheelsDao()
            if(dao.getLoads().isEmpty()){
                var load = 80f
                for (i in 20..28){
                    dao.saveLoad(Carga(i, load.toString()))
                    load+= 2.5f
                }
                load = 103f
                for (i in 29..35){
                    dao.saveLoad(Carga(i, load.toString()))
                    load+= 3f
                }
                load = 140f
                for (i in 40..52){
                    dao.saveLoad(Carga(i, load.toString()))
                    load+= 5f
                }
                load = 206f
                for (i in 53..58){
                    dao.saveLoad(Carga(i, load.toString()))
                    load+= 6f
                }
                load = 272f
                for (i in 59..63){
                    dao.saveLoad(Carga(i, load.toString()))
                    load+= 7f
                }
                load = 280f
                for (i in 64..74){
                    dao.saveLoad(Carga(i, load.toString()))
                    load+= 5f
                }
                dao.saveLoad(Carga(76, "400"))
                dao.saveLoad(Carga(77, "412"))
                dao.saveLoad(Carga(78, "425"))
                dao.saveLoad(Carga(79, "437"))
                dao.saveLoad(Carga(80, "450"))
                dao.saveLoad(Carga(81, "462"))
                dao.saveLoad(Carga(82, "475"))
                dao.saveLoad(Carga(83, "487"))
                load = 500f
                for (i in 84..88){
                    dao.saveLoad(Carga(i, load.toString()))
                    load+= 15f
                }
                dao.saveLoad(Carga(89, "580"))
                dao.saveLoad(Carga(90, "600"))
                dao.saveLoad(Carga(91, "615"))
                load = 630f
                for (i in 92..98){
                    dao.saveLoad(Carga(i, load.toString()))
                    load+= 20f
                }
                load = 775f
                for (i in 99..107){
                    dao.saveLoad(Carga(i, load.toString()))
                    load+= 25f
                }
                load = 1000f
                for (i in 108..114){
                    dao.saveLoad(Carga(i, load.toString()))
                    load+= 30f
                }
            }
        }
    }
}