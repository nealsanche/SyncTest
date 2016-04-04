package com.robotsandpencils.androidtemplate.managers;

import com.robotsandpencils.androidtemplate.App;
import com.robotsandpencils.androidtemplate.Constants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import rx.schedulers.Schedulers;

/**
 * Created by nealsanche on 2016-03-24.
 */
public class MockWebManager {
    private final App mContext;
    private boolean mSimulatedServerEnabled;
    private String mBaseUrl;
    private static MockWebManager mInstance;

    private MockWebServer mWebServer;

    public MockWebManager(App app) {
        mContext = app;
        mInstance = this;

        PreferencesManager preferences = new PreferencesManager(mContext);

        // Default to having the mock web enabled
        if (preferences.getBoolean(Constants.PREF_MOCK_WEB_ENABLED, true)) {
            mSimulatedServerEnabled = true;
            mWebServer = new MockWebServer();
            Object urlLock = new Object();
            Schedulers.io().createWorker().schedule(() -> {
                try {
                    mWebServer.start(8080);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to start web server.");
                }
                HttpUrl url = mWebServer.url("/api/v1/");
                mBaseUrl = url.toString();
                synchronized(urlLock) {
                    urlLock.notifyAll();
                }
            });
            synchronized(urlLock) {
                try {
                    urlLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            installDispatcher(mWebServer);
        }

    }

    private void installDispatcher(MockWebServer webServer) {
        webServer.setDispatcher(new DebugWebDispatcher(mContext));
    }

    public static MockWebManager getInstance() {
        return mInstance;
    }

    public boolean isSimulatedServerEnabled() {
        return mSimulatedServerEnabled;
    }

    public String getBaseUrl() {
        if (mBaseUrl == null) {
            throw new IllegalStateException("Mocked web server not initialized.");
        }
        return mBaseUrl;
    }
}
