package com.robotsandpencils.androidtemplate.modules;

import com.robotsandpencils.androidtemplate.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by nealsanche on 2016-06-15.
 */
@Module
public class DbModule {
    @Provides
    @Singleton
    public RealmConfiguration provideRealmConfiguration(App app) {
        return new RealmConfiguration.Builder(app)
                .deleteRealmIfMigrationNeeded()
                .name("app-db")
                .schemaVersion(1)
                .build();
    }

    @Provides
    public Realm provideRealm(RealmConfiguration realmConfiguration) {
        return Realm.getInstance(realmConfiguration);
    }
}
