package com.edxavier.wheels_equivalent;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
/**
 * Created by Eder Xavier Rojas on 16/11/2015.
 */
public class MyApplication extends android.app.Application {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-70090724-5";

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(800);
        tracker = analytics.newTracker(PROPERTY_ID); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
    }

    public synchronized  Tracker getTracker() {
        return tracker;
    }
}
