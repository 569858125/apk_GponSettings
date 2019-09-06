package com.konka.lee.gponsetting;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings({"unused"})
public class GPONServerManager {

    private final static int CONNECT_TIMEOUT = 60;
    private final static int READ_TIMEOUT = 100;
    private final static int WRITE_TIMEOUT = 60;
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient mClient;

    private GPONServerEventListener mListener = null;


    private GPONServerManager() {
        mClient = new OkHttpClient();
    }

    public static GPONServerManager getInstance() {
        return ManagerSingleton.INSTANCE.getInstance();
    }

    public void postInfo(String url, String json) throws IOException {
        LogUtil.d("Now post the json to server: " + json);
        new Thread(new PostRunnable(url, json)).start();
    }

    public void getInfo(String url) {
        new Thread(new GetRunnable(url)).start();
    }

    public void registerEventListener(GPONServerEventListener listener) {
        mListener = listener;
    }

    private enum ManagerSingleton {
        INSTANCE;
        private GPONServerManager mManager;

        ManagerSingleton() {
            mManager = new GPONServerManager();
        }

        public GPONServerManager getInstance() {
            return mManager;
        }
    }


    class GetRunnable implements Runnable {
        String mURL;

        GetRunnable(String mURL) {
            this.mURL = mURL;
        }

        @Override
        public void run() {
            Request request = new Request.Builder()
                    .url(mURL)
                    .get()
                    .build();
            Call call = mClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (mListener != null)
                        mListener.onGetFailure(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            if (mListener != null)
                                mListener.onGetResponse(response.body().string());
                        } catch (NullPointerException | IOException e) {
                            LogUtil.e(e.getMessage());
                            e.printStackTrace();
                        }

                    } else {
                        if (mListener != null)
                            mListener.onGetResponse("GPON server onResponse failure! Code(" + response.code() + ")");
                    }
                }
            });
        }
    }

    class PostRunnable implements Runnable {
        String mURL;
        String mJSONString;

        PostRunnable(String mURL, String mJSONString) {
            this.mURL = mURL;
            this.mJSONString = mJSONString;
        }

        @Override
        public void run() {
            RequestBody body = RequestBody.create(JSON, mJSONString);
            Request request = new Request.Builder()
                    .url(mURL)
                    .post(body)
                    .build();
            Call call = mClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (mListener != null)
                        mListener.onPostFailure(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            if (mListener != null)
                                mListener.onPostResponse(response.body().string());
                        } catch (NullPointerException | IOException e) {
                            LogUtil.e(e.getMessage());
                            e.printStackTrace();
                        }

                    } else {
                        if (mListener != null)
                            mListener.onPostResponse("GPON server onResponse failure! Code(" + response.code() + ")");
                    }
                }
            });
        }
    }
}
