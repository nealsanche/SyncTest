package com.robotsandpencils.synctest.modules;

import android.support.annotation.NonNull;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.robotsandpencils.synctest.App;
import com.robotsandpencils.synctest.managers.PreferencesManager;
import com.robotsandpencils.synctest.model.CanBeNew;
import com.robotsandpencils.synctest.model.Message;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmObject;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Module for providing network related dependencies.
 * <p>
 * Created by neal on 2015-11-30.
 */
@Module
public class NetModule {
    private final String mNetworkEndpoint;

    private final App mApp;

    public NetModule(App context, String networkEndpoint) {
        mApp = context;
        mNetworkEndpoint = networkEndpoint;
    }

    @Provides
    @Singleton
    public PreferencesManager providePreferencesManager(App context) {
        return new PreferencesManager(context);
    }

    @Singleton
    @Provides
    public App provideApp() {
        return mApp;
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return getGsonBuilderInstance()
                .registerTypeAdapter(Message.class, new IdRemovingSerializer<Message>())
                .create();
    }


    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient() {
        // Set up some caching
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(mApp.getCacheDir(), cacheSize);

        // Set up some logging
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .build();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(Gson gson, OkHttpClient client) {

        return new Retrofit.Builder()
                .baseUrl(mNetworkEndpoint)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @NonNull
    private GsonBuilder getGsonBuilderInstance() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonUtcDateAdapter())
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls();
    }

    /**
     * A generic class that takes anything extending CanBeNew interface so that
     * we can remove the id field of new objects when sending them over the wire.
     *
     * @param <T> Any class that implements CanBeNew
     */
    private class IdRemovingSerializer<T extends CanBeNew> implements JsonSerializer<T> {
        @Override
        public JsonElement serialize(T obj, Type type, JsonSerializationContext jsc) {
            JsonObject jObj = (JsonObject) getGsonBuilderInstance().create().toJsonTree(obj);
            if (obj.isNew()) {
                jObj.remove("id");
            }
            return jObj;
        }
    }

    /**
     * A Date adapter that makes sure dates are received and sent in UTC and in ISO8601 format.
     */
    private class GsonUtcDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

        GsonUtcDateAdapter() {
        }

        @Override
        public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(ISO8601Utils.format(date));
        }

        @Override
        public synchronized Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
            try {
                return ISO8601Utils.parse(jsonElement.getAsString(), new ParsePosition(0));
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }


}
