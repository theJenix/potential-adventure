package com.punventure.punadventure.svc;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

@SuppressLint("DefaultLocale")
public class GsonClient {

    private static final String TAG = "GsonClient";

    public GsonClient() {
        // TODO Auto-generated constructor stub
    }
    private <T> String resourceName(Class<T> clazz) {
        return clazz.getSimpleName().toLowerCase() + "s.json";
    }

    public <T> List<T> list(Class<T> clazz, Map<String, Object> params) throws IOException {
//        URL url = new URL("http://punadv.herokuapp.com/" + resourceName(clazz) + buildParams(params));
        URL url = new URL("http://143.215.113.33:3000/" + resourceName(clazz) + buildParams(params));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Gson gson = new Gson();
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, 0);
        @SuppressWarnings("unchecked")
        T[] objects = (T[]) gson.fromJson(new InputStreamReader(conn.getInputStream()), array.getClass());
        return Arrays.asList(objects);
    }
    
    public <T> void postImage(long id, String path) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://192.168.1.7:3000/notes/" + id + "/image");
        MultipartEntityBuilder build = MultipartEntityBuilder.create();
        File file = new File(path);

        build.addPart("image", new FileBody(file));

        HttpEntity ent = build.build();
        post.setEntity(ent);
        HttpResponse resp = null;
        try
        {
            resp = client.execute(post);
            HttpEntity resEnt = resp.getEntity();
            Log.w(TAG, EntityUtils.toString(resEnt));
        }
        catch(Exception e)
        {
            Log.wtf(TAG, e);
        }

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
                first = false;
            }
            return builder.toString();
        }
    }
}
