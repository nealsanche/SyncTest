package com.robotsandpencils.androidtemplate.net;

import com.robotsandpencils.androidtemplate.model.Version;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Network API for the Belterra Time server.
 *
 * Created by nealsanche on 2016-03-16.
 */
public interface BelterraAPI {

    @GET("login/auth")
    Observable<Response<Void>> getLogin();

    @GET("check/version")
    Observable<Version> getVersion();
}
