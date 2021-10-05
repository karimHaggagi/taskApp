package com.elprog.taskandroid.db;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit = null;

    public static RestApiService getApiService() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(7000, TimeUnit.SECONDS);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        client.addInterceptor(chain -> {
            Request request = chain
                    .request()
                    .newBuilder()
                  .addHeader("Accept", "application/json").build();
            return chain.proceed(request);
        });

        client.addInterceptor(httpLoggingInterceptor);
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .client(client.build())
                    .baseUrl("https://elsayedmustafa.github.io/HyperoneWebservice/")
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())

                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RestApiService.class);
    }

}
