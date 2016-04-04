package com.robotsandpencils.androidtemplate.util;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.Interceptor;

/**
 * Provides a class that hooks stetho into the app in debug mode, and you'll also notice
 * an empty implementation in the release tree that does nothing. These should be the
 * only methods that reference stetho at all.
 * <p/>
 * Created by nealsanche on 15-07-21.
 */
public class StethoHelper {

    public static void initializeStetho(Context context) {
        Stetho.initialize(
                Stetho.newInitializerBuilder(context)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                        .build());
    }

    public static Interceptor getStethoInterceptor() {
        return new StethoInterceptor();
    }
}
