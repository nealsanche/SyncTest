package com.robotsandpencils.androidtemplate.managers;

import com.robotsandpencils.androidtemplate.App;
import com.robotsandpencils.androidtemplate.model.Version;
import com.robotsandpencils.androidtemplate.net.RemoteAPI;
import com.squareup.otto.Bus;

import io.realm.RealmConfiguration;
import rx.Observable;

/**
 * Created by nealsanche on 2016-03-16.
 */
public class DataManager {

    private final App mContext;
    private final RemoteAPI mApi;
    private final RealmConfiguration mRealmConfiguration;
    private final Bus mBus;

    public DataManager(App context, RemoteAPI api, RealmConfiguration realmConfiguration, Bus bus) {
        mContext = context;
        mApi = api;
        mRealmConfiguration = realmConfiguration;
        mBus = bus;
    }

    public Observable<Version> getVersion() {
        return mApi.getVersion();
    }
}
