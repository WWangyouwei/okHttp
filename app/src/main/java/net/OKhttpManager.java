package net;

import android.os.Handler;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * 姓名：王有为
 * 时间：2017/9/25.
 */

public class OKhttpManager {
    private OkHttpClient mClient;
    private volatile static OKhttpManager sManager;//防止多个线程同时访问
    private static Handler handler;

    public OKhttpManager() {
        mClient = new OkHttpClient();
        handler = new Handler();
    }

    public static OKhttpManager getInstance() {
        OKhttpManager instance = null;
        if (sManager == null) {
            synchronized (OKhttpManager.class) {
                if (instance == null) {
                    instance = new OKhttpManager();
                    sManager = instance;
                }
            }
        }
        return instance;
    }

    public interface Func1 {
        void onResponse(String result);
    }

    public interface Func2 {
        void onResponse(byte[] result);
    }

    public interface Func3 {
        void onResponse(JSONObject jsonObject);
    }

    private static void onSuccessJsonStringMethod(final String jsonValue, final Func1 callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(jsonValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void onSuccessJsonObjectMethod(final String jsonValue, final Func3 callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(new JSONObject(jsonValue));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void onSuccessByteMethod(final byte[] data, final Func2 callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(data);
                }
            }
        });

    }
    public  void  asyncJsonStringByURL(String url,final Func1 callBack){
        final Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

              if (response!=null&&response.isSuccessful()){
                        onSuccessJsonStringMethod(response.body().string(),callBack);
                    }
                }

        });
    }
    public void asyncJsonObjectByURL(String url, final Func3 callBack) {
        final Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callBack);
                }
            }
        });

    }
    public void asyncGetByteByURL(String url, final Func2 callBack) {
        final Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessByteMethod(response.body().bytes(), callBack);
                }
            }
        });

    }
    public void sendComplexForm(String url, Map<String,String> params, final Func1 callBack){
//        FormBody.Builder form_builder=new FormBody.Builder();//表单对象包含以input开始的对象以html为主
//        非空键值对
        FormEncodingBuilder form_builder = new FormEncodingBuilder();
        if (params != null && !params.isEmpty()) {
            for(Map.Entry<String,String> entry : params.entrySet()){
                form_builder.add(entry.getKey(),entry.getValue());
            }
        }

        RequestBody request_body = form_builder.build();
        Request request = new Request.Builder().url(url).post(request_body).build();//采用post方式提交
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response!=null&&response.isSuccessful()){
                    onSuccessJsonStringMethod(response.body().string(), callBack);
                }
            }
        });

    }

}