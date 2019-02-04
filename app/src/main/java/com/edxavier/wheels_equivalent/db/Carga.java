package com.edxavier.wheels_equivalent.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Eder Xavier Rojas on 09/04/2018.
 */

@Table(database = AppDB.class)
public class Carga  extends BaseModel {
    @PrimaryKey
    public int id_carga;
    @Column
    public String valor;

    public Carga(int id_carga, String valor) {
        this.id_carga = id_carga;
        this.valor = valor;
    }

    public Carga() {
    }
    @Override
    public String toString() {
        return String.valueOf(id_carga);
    }
}
