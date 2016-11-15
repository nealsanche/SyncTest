package com.robotsandpencils.synctest.managers;

import com.google.gson.Gson;
import com.robotsandpencils.synctest.App;
import com.robotsandpencils.synctest.model.Message;

import java.nio.charset.Charset;

import javax.inject.Inject;

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

    @Inject
    Gson gson;
    private int mId = 1;

    public DebugWebDispatcher(App context) {
        mContext = context;
    }

    @Override
    public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        if (gson == null) {
            mContext.getUserComponent().inject(this);
        }

        Timber.d("Handling %s request: %s", request.getMethod(), request.getPath());

        if (request.getMethod().equals("GET")) {
            switch (request.getPath()) {
                case "/api/v1/login/auth":
                    return new MockResponse().setResponseCode(200);
                case "/api/v1/check/version":
                    return new MockResponse().setResponseCode(200).setBody("{ \"version\": 9 }").setHeader("Content-Type", "application/json");
            }
        } else if (request.getMethod().equals("POST")) {
            switch (request.getPath()) {
                case "/api/v1/sync/message":
                    String messageJson = request.getBody().readString(Charset.forName("UTF-8"));
                    Timber.d("Message json: %s", messageJson);

                    Message message = gson.fromJson(messageJson, Message.class);

                    if (message.getId() == 0) {
                        message.setId(mId++);
                    }

                    return new MockResponse().setResponseCode(200)
                            .setBody(gson.toJson(message))
                            .setHeader("Content-Type", "application/json");
            }
        }

        return new MockResponse().setResponseCode(404);
    }
}
