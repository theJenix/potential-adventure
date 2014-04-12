package com.punventure.punadventure.svc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.google.gson.Gson;

@SuppressLint("DefaultLocale")
public class GsonClient {

    public GsonClient() {
        // TODO Auto-generated constructor stub
    }
    private <T> String resourceName(Class<T> clazz) {
        return clazz.getSimpleName().toLowerCase() + "s.json";
    }

    public <T> List<T> list(Class<T> clazz, Map<String, Object> params) throws IOException {
        URL url = new URL("http://punadv.herokuapp.com/" + resourceName(clazz) + buildParams(params));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Gson gson = new Gson();
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, 0);
        @SuppressWarnings("unchecked")
        T[] objects = (T[]) gson.fromJson(new InputStreamReader(conn.getInputStream()), array.getClass());
        return Arrays.asList(objects);
    }
    
    private String buildParams(Map<String, Object> params) {
        if (params.isEmpty()) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder("?");
            boolean first = true;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (!first) {
                    builder.append("&");
                }
                builder.append(entry.getKey()).append("=").append(entry.getValue().toString());
            }
            return builder.toString();
        }
    }
}
