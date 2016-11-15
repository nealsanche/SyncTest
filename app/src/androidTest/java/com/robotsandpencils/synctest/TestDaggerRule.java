package com.robotsandpencils.synctest;

import android.support.test.InstrumentationRegistry;

import com.robotsandpencils.synctest.modules.DbModule;
import com.robotsandpencils.synctest.modules.NetModule;
import com.robotsandpencils.synctest.modules.UserComponent;
import com.robotsandpencils.synctest.modules.UserModule;

import java.util.UUID;

import io.realm.RealmConfiguration;
import it.cosenonjaviste.daggermock.DaggerMockRule;

/**
 * Created by nealsanche on 2016-06-15.
 */

public class TestDaggerRule extends DaggerMockRule<UserComponent> {

    public TestDaggerRule() {
        super(UserComponent.class,
                new UserModule(),
                new NetModule(getApp(), getNetworkEndpoint()),
                new TestDbModule()
        );

        set(new ComponentSetter<UserComponent>() {
            @Override
            public void setComponent(UserComponent userComponent) {
                getApp().setUserComponent(userComponent);
            }
        });
    }

    public static App getApp() {
        return (App) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    }

    /**
     * Use the internal test web server for data.
     *
     * @return
     */
    private static String getNetworkEndpoint() {
        return "http://localhost:8080/api/v1/";
    }

    /**
     * Override the realm configuration for the tests so that each test
     * module gets a clean copy of the database.
     */
    public static class TestDbModule extends DbModule {
        @Override
        public RealmConfiguration provideRealmConfiguration(App app) {

            return new RealmConfiguration.Builder(app)
                    .schemaVersion(1)
                    .deleteRealmIfMigrationNeeded()
                    .name("test.realm" + UUID.randomUUID().toString())
                    .build();
        }
    }
}
