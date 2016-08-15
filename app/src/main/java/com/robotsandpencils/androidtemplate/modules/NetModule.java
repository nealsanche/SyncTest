package com.robotsandpencils.androidtemplate.modules;

import com.robotsandpencils.androidtemplate.App;
import com.robotsandpencils.androidtemplate.managers.PreferencesManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Module for providing network related dependencies.
 * <p>
 * Created by neal on 2015-11-30.
 */
@Module
public class NetModule {
    private final String mNetworkEndpoint;

    private final App mApp;

    public NetModule(App context, String networkEndpoint) {
        mApp = context;
        mNetworkEndpoint = networkEndpoint;
    }

    @Provides
    @Singleton
    PreferencesManager providePreferencesManager(App context) {
        return new PreferencesManager(context);
    }

    @Singleton
    @Provides
    App provideApp() {
        return mApp;
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient() {
        // Set up some caching
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(mApp.getCacheDir(), cacheSize);

        // Set up some logging
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .build();

        return okHttpClient;
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient client) {

        return new Retrofit.Builder()
                .baseUrl(mNetworkEndpoint)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
