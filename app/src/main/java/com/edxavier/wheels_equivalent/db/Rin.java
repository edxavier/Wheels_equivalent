package com.edxavier.wheels_equivalent.db;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Eder Xavier Rojas on 09/04/2018.
 */

@Table(database = AppDB.class)
public class Rin  extends BaseModel {
    @PrimaryKey
    int rin;

    public Rin(int rin) {
        this.rin = rin;
    }

    public Rin() {
    }
    @Override
    public String toString() {
        return String.valueOf(rin);
    }
}
