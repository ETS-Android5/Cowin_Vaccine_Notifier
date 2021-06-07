package io.realworld.android.api;

import io.realworld.android.api.services.CowinAPIs;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CowinClient {
    private final String BASE_URL = "https://cdn-api.co-vin.in/api/v2/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public CowinAPIs api = retrofit.create(CowinAPIs.class);
}