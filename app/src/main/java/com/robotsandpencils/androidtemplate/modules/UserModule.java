package com.robotsandpencils.androidtemplate.modules;

import com.robotsandpencils.androidtemplate.App;
import com.robotsandpencils.androidtemplate.managers.ActionManager;
import com.robotsandpencils.androidtemplate.managers.DataManager;
import com.robotsandpencils.androidtemplate.net.RemoteAPI;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;

/**
 * Created by neal on 2015-11-30.
 */
@Module
public class UserModule {

    @Singleton
    @Provides
    public RemoteAPI getServerAPI(Retrofit retrofit) {
        return retrofit.create(RemoteAPI.class);
    }

    @Singleton
    @Provides
    public DataManager getDataManager(App context, RemoteAPI api, RealmConfiguration realmConfiguration, Bus bus) {
        return new DataManager(context, api, realmConfiguration, bus);
    }

    @Singleton
    @Provides
    public Bus getBus() {
        return new Bus();
    }

    @Singleton
    @Provides
    public ActionManager provideActionManager() {
        return new ActionManager();
    }

}
