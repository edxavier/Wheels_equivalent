package com.edxavier.wheels_equivalent;

import android.content.ContextWrapper;

import com.google.android.gms.ads.MobileAds;
import com.pixplicity.easyprefs.library.Prefs;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Eder Xavier Rojas on 16/11/2015.
 */
public class MyApplication extends android.app.Application {

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-70090724-5";

    @Override
    public void onCreate() {
        super.onCreate();
        //MultiDex.install(this);
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        FlowManager.init(this);
        MobileAds.initialize(this, "ca-app-pub-9964109306515647~1012826218");
    }

}
