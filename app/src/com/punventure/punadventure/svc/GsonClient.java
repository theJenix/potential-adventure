package com.punventure.punadventure.svc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.punventure.punadventure.model.Note;

@SuppressLint("DefaultLocale")
public class GsonClient {

    private static final String TAG = "GsonClient";
    private Context context;

    public GsonClient(Context context) {
        this.context = context;
    }

    private <T> String resourceName(Class<T> clazz) {
        return clazz.getSimpleName().toLowerCase() + "s.json";
    }

//    String host = "143.215.113.33:3000";
    String host = "punadv.herokuapp.com";
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

    public <T> void post(Note obj) {
        HttpURLConnection connection = null;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            URL url = new URL("http://" + host + "/notesapp.json");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length",
                    Integer.toString(json.length()));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(json.getBytes());
            connection.connect();
//            if (connection.getResponseCode() != 302) {
//                Log.wtf(TAG, "no redirect!?  denied!");
//                return;
//            }
//            String redir = connection.getHeaderField("Location");
//            String id = redir.substring(redir.lastIndexOf("/") + 1);
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            Note ret = gson.fromJson(reader, Note.class);
            obj.setId(ret.getId());
            // this.retries = 0;
        } catch (IOException ex) {
            // this.retries++;
            // if (this.retries >= 5) {
            throw new RuntimeException(ex);
            // }

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void postAudio(long id, String path) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + host + "/notes/" + id + "/audio");
        post.addHeader("content_type","audio/mp4");
        MultipartEntityBuilder build = MultipartEntityBuilder.create();
        build.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);  
        
        File file = new File(path);

        try
        {
            build.addPart("fileName", new StringBody(file.getName()));
            build.addPart("audio", new FileBody(file));

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
    public String fetchFile(String file_path) throws IOException {
        if (file_path.startsWith("public/")) { //HAXX
            file_path = file_path.substring(7);
        }
        URL url = new URL("http://" + host + "/" + file_path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String fileName = file_path.substring(file_path.lastIndexOf("/") + 1);
        FileOutputStream fos = this.context.openFileOutput(fileName, 0);
        byte [] buffer = new byte[4096];
        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
        int bytesRead = 0;
        while (( bytesRead = bis.read(buffer, 0, 4096)) > 0) {
            fos.write(buffer, 0, bytesRead);
        }
        fos.flush();
        fos.close();
        return this.context.getFileStreamPath(fileName).getCanonicalPath();
    }    
}
