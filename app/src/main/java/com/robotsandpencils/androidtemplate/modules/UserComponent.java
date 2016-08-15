package com.robotsandpencils.androidtemplate.modules;

import com.robotsandpencils.androidtemplate.App;
import com.robotsandpencils.androidtemplate.activities.DashboardActivity;
import com.robotsandpencils.androidtemplate.managers.DataManager;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;

/**
 * Created by neal on 2015-11-30.
 */
@Singleton
@Component(modules = {UserModule.class, NetModule.class, DbModule.class})
public interface UserComponent {

    Realm realm();

    void inject(App app);

    void inject(DataManager dataManager);
    void inject(DashboardActivity dashboardActivity);
}
