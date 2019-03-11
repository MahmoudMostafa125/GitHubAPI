package com.example.githubapi.Api;

import com.example.githubapi.model.DefaultResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("users/square/repos?")
    Call<List<DefaultResponse>> GitApi(

            @Query("page") String page, @Query("per_page") String per_page, @Query("access_token") String access_token
    );
}
