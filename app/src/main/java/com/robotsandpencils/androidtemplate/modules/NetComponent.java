package com.robotsandpencils.androidtemplate.modules;


import com.robotsandpencils.androidtemplate.App;
import com.robotsandpencils.androidtemplate.managers.PreferencesManager;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by neal on 2015-11-30.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    App app();
    Retrofit retrofit();
    PreferencesManager preferences();
}
