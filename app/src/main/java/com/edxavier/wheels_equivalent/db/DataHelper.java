package com.edxavier.wheels_equivalent.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by Eder Xavier Rojas on 10/04/2018.
 */

public class DataHelper {
    public static boolean perfilExiste(int perfil){
        Perfil p = SQLite.select()
                .from(Perfil.class)
                .where(Perfil_Table.perfil.eq(perfil))
                .querySingle();
        return p != null;
    }
    public static boolean anchoExiste(int anchura){
        Ancho a = SQLite.select()
                .from(Ancho.class)
                .where(Ancho_Table.ancho.eq(anchura))
                .querySingle();
        return a != null;
    }
    public static boolean rinExiste(int rin){
        Rin r = SQLite.select()
                .from(Rin.class)
                .where(Rin_Table.rin.eq(rin))
                .querySingle();
        return r != null;
    }


    public static boolean cargaExiste(int id_carga){
        Carga r = SQLite.select()
                .from(Carga.class)
                .where(Carga_Table.id_carga.eq(id_carga))
                .querySingle();
        return r != null;
    }

    public static boolean velocidadExiste(String id_vel){
        Velocidad r = SQLite.select()
                .from(Velocidad.class)
                .where(Velocidad_Table.id_velocidad.eq(id_vel))
                .querySingle();
        return r != null;
    }
    public static List<Perfil> cargarPerfiles(){
        return  SQLite.select()
                .from(Perfil.class)
                .queryList();
    }

    public static List<Ancho> cargarAnchos(){
        return  SQLite.select()
                .from(Ancho.class)
                .queryList();
    }
    public static List<Rin> cargarRines(){
        return  SQLite.select()
                .from(Rin.class)
                .queryList();
    }
    public static List<Carga> cargarCargas(){
        return  SQLite.select()
                .from(Carga.class)
                .queryList();
    }

    public static List<Velocidad> cargarVelocidades(){
        return  SQLite.select()
                .from(Velocidad.class)
                .queryList();
    }


}
