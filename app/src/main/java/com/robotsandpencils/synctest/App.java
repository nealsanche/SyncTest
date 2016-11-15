package com.robotsandpencils.synctest;

import android.app.Application;

import com.robotsandpencils.synctest.managers.MockWebManager;
import com.robotsandpencils.synctest.modules.DaggerUserComponent;
import com.robotsandpencils.synctest.modules.NetModule;
import com.robotsandpencils.synctest.modules.UserComponent;
import com.robotsandpencils.synctest.modules.UserModule;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by nealsanche on 2016-03-16.
 */
public class App extends Application {

    private UserComponent mUserComponent;
    private MockWebManager mMockWebManager;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Realm
        Realm.init(this);

        if (BuildConfig.DEBUG) {
            mMockWebManager = new MockWebManager(this);
        }

        if (mUserComponent == null) {
            mUserComponent = DaggerUserComponent.builder()
                    .userModule(new UserModule())
                    .netModule(new NetModule(this, getNetworkEndpoint()))
                    .build();
        }

        mUserComponent.inject(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }

    private String getNetworkEndpoint() {

        if (BuildConfig.DEBUG && mMockWebManager.isSimulatedServerEnabled()) {
            return mMockWebManager.getBaseUrl();
        }

        return getResources().getString(R.string.endpoint);
    }

    /**
     * Get the UserComponent to inject a class with
     *
     * @return a UserComponent
     */
    public UserComponent getUserComponent() {
        return mUserComponent;
    }

    /**
     * This is used to inject a component for testing if
     * required.
     *
     * @param component a usercomponent for testing with
     */
    public void setUserComponent(UserComponent component) {
        mUserComponent = component;
    }
}
