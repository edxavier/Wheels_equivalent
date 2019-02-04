package com.edxavier.wheels_equivalent.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Eder Xavier Rojas on 09/04/2018.
 */

@Table(database = AppDB.class)
public class Velocidad  extends BaseModel {
    @PrimaryKey
    String id_velocidad;
    @Column
    String valor;
    @Column
    int index;

    public Velocidad(String id_velocidad, String valor, int index) {
        this.id_velocidad = id_velocidad;
        this.valor = valor;
        this.index = index;
    }

    public Velocidad() {
    }
    @Override
    public String toString() {
        return String.valueOf(id_velocidad);
    }

    public String getValor() {
        return valor;
    }
}
