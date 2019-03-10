package com.example.githubapi.Api;

import com.example.githubapi.model.DefaultResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface  Api {

    @GET("users/square/repos")
    Call<DefaultResponse> GitApi();
}
