package com.casapan.pedidos.App;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jerry on 3/21/2018.
 */

public class GlobalApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
