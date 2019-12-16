package com.example.lazycashier;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    public static boolean hasNetwork() {
        return instance.isNetworkConnected();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (instance == null) {
            instance = this;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
