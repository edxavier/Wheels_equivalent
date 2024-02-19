package com.edxavier.wheels_equivalent

import android.app.Application
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.pixplicity.easyprefs.library.Prefs

/**
 * Created by Eder Xavier Rojas on 16/11/2015.
 */
class MyApplication : Application() {
    private lateinit var appOpenManager:AppOpenManager

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        //MultiDex.install(this);
        Prefs.Builder()
                .setContext(this)
                .setMode(MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .build()

        //MobileAds.initialize(this, "ca-app-pub-9964109306515647~1012826218")
        MobileAds.initialize(this)
        val requestConfig = RequestConfiguration.Builder()
                .setTestDeviceIds(arrayOf(
                        "AC5F34885B0FE7EF03A409EB12A0F949",
                        AdRequest.DEVICE_ID_EMULATOR
                ).toList())
                .build()
        MobileAds.setRequestConfiguration(requestConfig)
        appOpenManager = AppOpenManager(this);

    }

    companion object {
        // The following line should be changed to include the correct property id.
        private const val PROPERTY_ID = "UA-70090724-5"
    }
}