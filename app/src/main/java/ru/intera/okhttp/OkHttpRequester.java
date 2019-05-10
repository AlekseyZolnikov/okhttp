package ru.intera.okhttp;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpRequester {

    private final OnResponseCompleted listener;

    public OkHttpRequester(OnResponseCompleted listener) {
        this.listener = listener;
    }

    public void run(String url) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {

            Handler handler = new Handler();

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("OkHttpRequester", "Load fail", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String answer = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onCompleted(answer);
                    }
                });
            }
        });
    }

    public interface OnResponseCompleted {
        void onCompleted(String content);
    }
}
