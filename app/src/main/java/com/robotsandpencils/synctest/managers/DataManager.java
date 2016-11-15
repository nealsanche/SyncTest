package com.robotsandpencils.synctest.managers;

import com.robotsandpencils.synctest.App;
import com.robotsandpencils.synctest.model.Version;
import com.robotsandpencils.synctest.net.RemoteAPI;
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
