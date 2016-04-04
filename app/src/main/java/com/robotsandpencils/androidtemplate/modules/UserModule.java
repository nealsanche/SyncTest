package com.robotsandpencils.androidtemplate.modules;

import com.robotsandpencils.androidtemplate.App;
import com.robotsandpencils.androidtemplate.managers.ActionManager;
import com.robotsandpencils.androidtemplate.managers.DataManager;
import com.robotsandpencils.androidtemplate.net.BelterraAPI;
import com.squareup.otto.Bus;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;

/**
 * Created by neal on 2015-11-30.
 */
@Module
public class UserModule {

    @UserScope
    @Provides
    BelterraAPI getServerAPI(Retrofit retrofit) {
        return retrofit.create(BelterraAPI.class);
    }

    @UserScope
    @Provides
    DataManager getDataManager(App context, BelterraAPI api, RealmConfiguration realmConfiguration, Bus bus) {
        return new DataManager(context, api, realmConfiguration, bus);
    }

    @UserScope
    @Provides
    Bus getBus() {
        return new Bus();
    }

    @Provides
    @UserScope
    RealmConfiguration provideRealmConfiguration(App app) {
        return new RealmConfiguration.Builder(app)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();
    }

    @Provides
    Realm provideRealm(RealmConfiguration realmConfiguration) {
        return Realm.getInstance(realmConfiguration);
    }

    @Provides
    @UserScope
    ActionManager provideActionManager() {
        return new ActionManager();
    }

}
