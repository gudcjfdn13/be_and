package com.sbs.jhs.be.and;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App {

    public static BeApiService getBeApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://39.119.128.155:8089")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        BeApiService beApiService = retrofit.create(BeApiService.class);

        return beApiService;
    }
}

