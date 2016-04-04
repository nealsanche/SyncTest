package com.robotsandpencils.androidtemplate;

import android.app.Application;

import com.robotsandpencils.androidtemplate.managers.MockWebManager;
import com.robotsandpencils.androidtemplate.modules.AppModule;
import com.robotsandpencils.androidtemplate.modules.DaggerNetComponent;
import com.robotsandpencils.androidtemplate.modules.DaggerUserComponent;
import com.robotsandpencils.androidtemplate.modules.NetComponent;
import com.robotsandpencils.androidtemplate.modules.NetModule;
import com.robotsandpencils.androidtemplate.modules.UserComponent;
import com.robotsandpencils.androidtemplate.modules.UserModule;
import com.robotsandpencils.androidtemplate.util.StethoHelper;

/**
 * Created by nealsanche on 2016-03-16.
 */
public class App extends Application {

    private static final String TAG = "App";

    private UserComponent mUserComponent;

    private MockWebManager mMockWebManager;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            mMockWebManager = new MockWebManager(this);
        }

        if (mUserComponent == null) {
            NetComponent netComponent = DaggerNetComponent.builder()
                    .appModule(new AppModule(this))
                    .netModule(new NetModule(this, getNetworkEndpoint()))
                    .build();
            mUserComponent = DaggerUserComponent.builder()
                    .netComponent(netComponent)
                    .userModule(new UserModule())
                    .build();
        }

        StethoHelper.initializeStetho(this);

        mUserComponent.inject(this);
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