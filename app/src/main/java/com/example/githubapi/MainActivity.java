package com.example.githubapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.githubapi.Api.RetrofitClient;
import com.example.githubapi.model.DefaultResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rec);

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().GitApi();
        call.enqueue(new Callback<DefaultResponse>() {


            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if (response.isSuccessful()) {
                    Log.d("resres", "onResponse: "+response.body().getArchiveUrl());
                }


            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.d("ERERERER", String.valueOf(t.getStackTrace()));

            }
        });
    }
}
