package com.unir.appapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Bitcoin {
    @GET("tobtc")
    Call<Double> getCurrentValue(@Query("currency") String moeda, @Query("value") int valor);

}
