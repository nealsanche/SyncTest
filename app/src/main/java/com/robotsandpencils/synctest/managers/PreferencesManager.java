package com.robotsandpencils.synctest.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Manages Preferences
 * <p>
 * Created by nealsanche on 2015-12-14.
 */
public class PreferencesManager {
    private final Context mContect;
    private final SharedPreferences mPreferences;

    public PreferencesManager(Context context) {
        mContect = context;
        mPreferences = context.getSharedPreferences("mainPreferences", Context.MODE_PRIVATE);
    }

    @SuppressLint("CommitPrefEdits")
    public void putBoolean(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    @SuppressLint("CommitPrefEdits")
    public void putString(String key, String value) {
        mPreferences.edit().putString(key, value).commit();
    }

    public String getString(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }

    @SuppressLint("CommitPrefEdits")
    public void remove(String key) {
        mPreferences.edit().remove(key).commit();
    }

    public void putFloat(String key, float f) {
        mPreferences.edit().putFloat(key, f).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return mPreferences.getFloat(key, defaultValue);
    }

    public void putDate(String key, Date date) {
        mPreferences.edit().putLong(key, date.getTime()).apply();
    }

    public Date getDate(String key, Date defaultDate) {
        if (mPreferences.contains(key)) {
            long time = mPreferences.getLong(key, 0);
            return new Date(time);
        } else {
            return defaultDate;
        }
    }

    public void removeAll(String prefix) {
        SharedPreferences.Editor edit = mPreferences.edit();
        for (String key : mPreferences.getAll().keySet()) {
            if (key.startsWith(prefix)) {
                edit.remove(key);
            }
        }
        edit.apply();
    }

    public boolean contains(String key) {
        return mPreferences.contains(key);
    }
}
