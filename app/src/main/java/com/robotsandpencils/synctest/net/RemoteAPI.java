package com.robotsandpencils.synctest.net;

import com.robotsandpencils.synctest.model.Version;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Network API for the server.
 *
 * Created by nealsanche on 2016-03-16.
 */
public interface RemoteAPI {

    @GET("login/auth")
    Observable<Response<Void>> getLogin();

    @GET("check/version")
    Observable<Version> getVersion();
}
