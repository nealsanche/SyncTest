package com.robotsandpencils.synctest.modules;

import com.robotsandpencils.synctest.App;
import com.robotsandpencils.synctest.activities.DashboardActivity;
import com.robotsandpencils.synctest.managers.DataManager;
import com.robotsandpencils.synctest.managers.DebugWebDispatcher;

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

    void inject(DebugWebDispatcher debugWebDispatcher);
}
