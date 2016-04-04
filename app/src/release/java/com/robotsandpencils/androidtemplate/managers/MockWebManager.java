package com.robotsandpencils.androidtemplate.managers;

import com.robotsandpencils.androidtemplate.App;

/**
 * Created by nealsanche on 2016-03-24.
 */
public class MockWebManager {
    private final App mContext;
    private static MockWebManager mInstance;

    public MockWebManager(App app) {
        mContext = app;
    }

    public static MockWebManager getInstance() {
        return mInstance;
    }

    public boolean isSimulatedServerEnabled() {
        return false;
    }

    public String getBaseUrl() {
        throw new IllegalStateException("Mocking not enabled in Release builds.");
    }
}
