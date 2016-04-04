package com.robotsandpencils.androidtemplate.modules;

import com.robotsandpencils.androidtemplate.App;
import com.robotsandpencils.androidtemplate.managers.PreferencesManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Top level module mostly for singletons.
 * <p>
 * Created by neal on 2015-11-30.
 */
@Module
public class AppModule {
    private final App mApp;

    public AppModule(App app) {
        mApp = app;
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
}
