package com.punventure.punadventure.svc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
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

    String host = "143.215.113.33:3000";
//    String host = "punadv.herokuapp.com";
    public <T> List<T> list(Class<T> clazz, Map<String, Object> params) throws IOException {
//        URL url = new URL("http://punadv.herokuapp.com/" + resourceName(clazz) + buildParams(params));
        URL url = new URL("http://" + host + "/" + resourceName(clazz) + buildParams(params));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Gson gson = new Gson();
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, 0);
        @SuppressWarnings("unchecked")
        T[] objects = (T[]) gson.fromJson(new InputStreamReader(conn.getInputStream()), array.getClass());
        return Arrays.asList(objects);
    }

    public <T> void post(T obj) {
        HttpURLConnection connection = null;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            URL url = new URL("http://" + host + "/" + resourceName(obj.getClass()));
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length",
                    Integer.toString(json.length()));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(json.getBytes());
            connection.connect();
            connection.getResponseCode();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            Pattern p = Pattern.compile("Id: (\\d*)");
            while (br.ready()) {
                String line = br.readLine();
                Matcher m = p.matcher(line);
                if (m.find()) {
                    int id = Integer.valueOf(m.group(1));
                    obj.getClass().getMethod("id", long.class).invoke(obj, id);
                    break;
                }
            }
            // this.retries = 0;
        } catch (IOException ex) {
            // this.retries++;
            // if (this.retries >= 5) {
            throw new RuntimeException(ex);
            // }
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void postAudio(long id, String audioPath) {
        // TODO Auto-generated method stub
        
    }

    public <T> void postImage(long id, String path) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + host + "/notes/" + id + "/image");
        post.addHeader("content_type","image/jpeg");
        MultipartEntityBuilder build = MultipartEntityBuilder.create();
        build.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);  
        
        File file = new File(path);

        try
        {
            build.addPart("fileName", new StringBody(file.getName()));
            build.addPart("image", new FileBody(file));

            post.setEntity(build.build());
            HttpResponse resp = null;
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
