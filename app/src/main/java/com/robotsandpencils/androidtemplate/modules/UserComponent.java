package com.robotsandpencils.androidtemplate.modules;

import com.robotsandpencils.androidtemplate.App;
import com.robotsandpencils.androidtemplate.activities.DashboardActivity;

import dagger.Component;

/**
 * Created by neal on 2015-11-30.
 */
@UserScope
@Component(dependencies = NetComponent.class, modules = UserModule.class)
public interface UserComponent {
    void inject(App app);
    void inject(DashboardActivity dashboardActivity);
}
