package com.punventure.punadventure.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;

public class Settings {

    private static final String NAME_KEY = "name";

    private static final String PREFS_NAME = "PunAdvSettings";

    private String name;
    
    private String deviceId;

    private Context context;

    private static Settings _instance;
    
    public static Settings instance() {
        return _instance;
    }

    public static Settings loadOrInit(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        _instance = new Settings(context);
        _instance.name = settings.getString(NAME_KEY, null);
        _instance.deviceId = Secure.getString(context.getContentResolver(),
                                Secure.ANDROID_ID); 
        return _instance;
    }

    public Settings(Context context) {
        this.context = context;
    }

    public void save() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(NAME_KEY, name);
        // Commit the edits!
        editor.commit();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
}
