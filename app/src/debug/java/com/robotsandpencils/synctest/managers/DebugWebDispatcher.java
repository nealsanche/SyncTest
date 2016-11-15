package com.robotsandpencils.synctest.managers;

import com.robotsandpencils.synctest.App;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import timber.log.Timber;

/**
 * A web dispatcher that is specific to this application, and where all of the supported
 * web methods can be simulated.
 * <p>
 * Created by nealsanche on 2016-03-24.
 */
public class DebugWebDispatcher extends Dispatcher {
    private final App mContext;

    public DebugWebDispatcher(App context) {
        mContext = context;
    }

    @Override
    public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

        Timber.d("Handling %s request: %s", request.getMethod(), request.getPath());

        switch (request.getPath()) {
            case "/api/v1/login/auth":
                return new MockResponse().setResponseCode(200);
            case "/api/v1/check/version":
                return new MockResponse().setResponseCode(200).setBody("{ \"version\": 9 }").setHeader("Content-Type", "application/json");
        }

        return new MockResponse().setResponseCode(404);
    }
}
