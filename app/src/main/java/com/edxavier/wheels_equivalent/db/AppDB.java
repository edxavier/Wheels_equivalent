package com.edxavier.wheels_equivalent.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Eder Xavier Rojas on 09/04/2018.
 */

@Database(name = AppDB.NAME, version = AppDB.VERSION)
public class AppDB {
    static final String NAME = "AppDatabase";
    static final int VERSION = 1;
}
