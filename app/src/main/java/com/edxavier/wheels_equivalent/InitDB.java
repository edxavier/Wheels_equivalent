package com.edxavier.wheels_equivalent;

import android.util.Log;

import com.edxavier.wheels_equivalent.db.Ancho;
import com.edxavier.wheels_equivalent.db.AppDB;
import com.edxavier.wheels_equivalent.db.Carga;
import com.edxavier.wheels_equivalent.db.DataHelper;
import com.edxavier.wheels_equivalent.db.Perfil;
import com.edxavier.wheels_equivalent.db.Rin;
import com.edxavier.wheels_equivalent.db.Velocidad;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eder Xavier Rojas on 09/04/2018.
 */

class InitDB {
    public static void InitPerfil(){
            List<Perfil> perfiles = new ArrayList<>();
            if(!DataHelper.perfilExiste(15)) {
                perfiles.add(new Perfil(15));
            }
            if(!DataHelper.perfilExiste(81)) {
                perfiles.add(new Perfil(81));
            }
            if(!DataHelper.perfilExiste(82)) {
                perfiles.add(new Perfil(82));
            }
            for (int i = 25; i <= 85; i += 5) {
                if(!DataHelper.perfilExiste(i)) {
                    perfiles.add(new Perfil(i));
                }
            }

            FastStoreModelTransaction<Perfil> trans = FastStoreModelTransaction
                    .insertBuilder(FlowManager.getModelAdapter(Perfil.class))
                    .addAll(perfiles)
                    .build();
            DatabaseDefinition database = FlowManager.getDatabase(AppDB.class);
            Transaction transaction = database.beginTransactionAsync(trans).build();
            try {
                transaction.execute();
            }catch (Exception e){}

    }

    public static void InitAncho(){
        List<Ancho> anchuras = new ArrayList<>();

        for (int i = 135; i <= 355; i += 5) {
            if(!DataHelper.anchoExiste(i)) {
                anchuras.add(new Ancho(i));
            }
        }

        FastStoreModelTransaction<Ancho> trans = FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(Ancho.class))
                .addAll(anchuras)
                .build();
        DatabaseDefinition database = FlowManager.getDatabase(AppDB.class);
        Transaction transaction = database.beginTransactionAsync(trans).build();
        try {
            transaction.execute();
        }catch (Exception e){}

    }

    public static void InitRines(){
        List<Rin> rines = new ArrayList<>();

        for (int i = 10; i <= 23; i++) {
            if(!DataHelper.rinExiste(i)) {
                rines.add(new Rin(i));
            }
        }

        FastStoreModelTransaction<Rin> trans = FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(Rin.class))
                .addAll(rines)
                .build();
        DatabaseDefinition database = FlowManager.getDatabase(AppDB.class);
        Transaction transaction = database.beginTransactionAsync(trans).build();
        try {
            transaction.execute();
        }catch (Exception e){}

    }

    public static void InitCargas(){
        List<Carga> cargas = new ArrayList<>();

        float valor_carga = 80;
        for (int i = 20; i <= 28; i++) {
            if(!DataHelper.cargaExiste(i)) {
                cargas.add(new Carga(i, String.valueOf(valor_carga)));
                valor_carga += 2.5f;
            }
        }

        valor_carga = 103f;
        for (int i = 29; i <= 35; i++) {
            if(!DataHelper.cargaExiste(i)) {
                cargas.add(new Carga(i, String.valueOf(valor_carga)));
                valor_carga += 3f;
            }
        }

        valor_carga = 140f;
        for (int i = 40; i <= 52; i++) {
            if(!DataHelper.cargaExiste(i)) {
                cargas.add(new Carga(i, String.valueOf(valor_carga)));
                valor_carga += 5f;
            }
        }
        valor_carga = 206f;
        for (int i = 53; i <= 58; i++) {
            if(!DataHelper.cargaExiste(i)) {
                cargas.add(new Carga(i, String.valueOf(valor_carga)));
                valor_carga += 6f;
            }
        }
        valor_carga = 272f;
        for (int i = 59; i <= 63; i++) {
            if(!DataHelper.cargaExiste(i)) {
                cargas.add(new Carga(i, String.valueOf(valor_carga)));
                valor_carga += 7f;
            }
        }

        valor_carga = 280f;
        for (int i = 64; i <= 74; i++) {
            if(!DataHelper.cargaExiste(i)) {
                cargas.add(new Carga(i, String.valueOf(valor_carga)));
                valor_carga += 5f;
            }
        }
        if(!DataHelper.cargaExiste(76)) {
            cargas.add(new Carga(76, "400"));
        }
        if(!DataHelper.cargaExiste(77)) {
            cargas.add(new Carga(77, "412"));
        }
        if(!DataHelper.cargaExiste(78)) {
            cargas.add(new Carga(78, "425"));
        }
        if(!DataHelper.cargaExiste(79)) {
            cargas.add(new Carga(79, "437"));
        }
        if(!DataHelper.cargaExiste(80)) {
            cargas.add(new Carga(80, "450"));
        }
        if(!DataHelper.cargaExiste(81)) {
            cargas.add(new Carga(81, "462"));
        }
        if(!DataHelper.cargaExiste(82)) {
            cargas.add(new Carga(82, "475"));
        }
        if(!DataHelper.cargaExiste(83)) {
            cargas.add(new Carga(83, "487"));
        }
        valor_carga = 500f;
        for (int i = 84; i <= 88; i++) {
            if(!DataHelper.cargaExiste(i)) {
                cargas.add(new Carga(i, String.valueOf(valor_carga)));
                valor_carga += 15f;
            }
        }
        if(!DataHelper.cargaExiste(89)) {
            cargas.add(new Carga(89, "580"));
        }if(!DataHelper.cargaExiste(90)) {
            cargas.add(new Carga(90, "600"));
        }if(!DataHelper.cargaExiste(91)) {
            cargas.add(new Carga(91, "615"));
        }
        valor_carga = 630f;
        for (int i = 92; i <= 98; i++) {
            if(!DataHelper.cargaExiste(i)) {
                cargas.add(new Carga(i, String.valueOf(valor_carga)));
                valor_carga += 20f;
            }
        }
        valor_carga = 775;
        for (int i = 99; i <= 107; i++) {
            if(!DataHelper.cargaExiste(i)) {
                cargas.add(new Carga(i, String.valueOf(valor_carga)));
                valor_carga += 25f;
            }
        }
        valor_carga = 1000;
        for (int i = 108; i <= 114; i++) {
            if(!DataHelper.cargaExiste(i)) {
                cargas.add(new Carga(i, String.valueOf(valor_carga)));
                valor_carga += 30f;
            }
        }


        FastStoreModelTransaction<Carga> trans = FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(Carga.class))
                .addAll(cargas)
                .build();
        DatabaseDefinition database = FlowManager.getDatabase(AppDB.class);
        Transaction transaction = database.beginTransactionAsync(trans).build();
        try {
            transaction.execute();
        }catch (Exception e){
            Log.e("EDER", e.getMessage());
        }

    }

    public static void InitVelocidades(){
        List<Velocidad> velocidades = new ArrayList<>();
        String[] codigos = { "B", "C", "D", "E","F", "G","J", "K","L", "M","N", "P","Q", "R","S", "T","U", "H","V", "ZR","W", "Y" };
        String[] valores = { "50", "60", "65", "70","80", "90","100", "110","120", "130","140", "150","160", "170","180", "190","200", "210","240", ">240","270", "300" };

        for (int i = 0; i < codigos.length; i++) {
            if(!DataHelper.velocidadExiste(codigos[i])) {
                velocidades.add(new Velocidad(codigos[i], valores[i], i));
            }
        }

        FastStoreModelTransaction<Velocidad> trans = FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(Velocidad.class))
                .addAll(velocidades)
                .build();
        DatabaseDefinition database = FlowManager.getDatabase(AppDB.class);
        Transaction transaction = database.beginTransactionAsync(trans).build();
        try {
            transaction.execute();
        }catch (Exception e){}

    }
}
