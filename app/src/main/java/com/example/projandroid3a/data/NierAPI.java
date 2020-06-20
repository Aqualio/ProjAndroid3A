package com.example.projandroid3a.data;

import com.example.projandroid3a.presentation.model.RestNierAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NierAPI {
    @GET("NierAutomata-API.json")
    Call<RestNierAPIResponse> getNierResponse();
}
