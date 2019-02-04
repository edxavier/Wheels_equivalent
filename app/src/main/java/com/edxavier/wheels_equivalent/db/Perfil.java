package com.edxavier.wheels_equivalent.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Eder Xavier Rojas on 09/04/2018.
 */

@Table(database = AppDB.class)
public class Perfil extends BaseModel {
    @PrimaryKey
    public int perfil;

    public Perfil(int perfil) {
        this.perfil = perfil;
    }

    public Perfil() {
    }
    @Override
    public String toString() {
        return String.valueOf(perfil);
    }
}
