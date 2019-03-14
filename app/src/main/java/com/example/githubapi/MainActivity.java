package com.example.githubapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.githubapi.Adapter.repoAdapter;
import com.example.githubapi.Api.RetrofitClient;
import com.example.githubapi.Utilities.internetConnection;
import com.example.githubapi.model.DefaultResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<DefaultResponse> repoList;
    repoAdapter adapter;
    Context cox;

    private int page_number = 1;
    private int item_count = 10;

    private Boolean isLoading = true;
    private int pastVisbleItem, visibleItemCount, TotalItemCount, PreviousTotal = 0;
    private int threshold = 10;

    LinearLayoutManager layoutManager;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cox = this;
        progressBar = findViewById(R.id.proBar);
        recyclerView = findViewById(R.id.rec);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = findViewById(R.id.refresh);

        if (!internetConnection.internetConnectionAvailable(1000)) {
            Toast.makeText(cox, "You Are Offline now", Toast.LENGTH_SHORT).show();
        }
        Call<List<DefaultResponse>> call = RetrofitClient.getInstance(cox).getApi()
                .GitApi(String.valueOf(page_number),
                        String.valueOf(item_count),
                        Constants.Token);
        call.enqueue(new Callback<List<DefaultResponse>>() {
            @Override
            public void onResponse(Call<List<DefaultResponse>> call, Response<List<DefaultResponse>> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        repoList = response.body();
                    }
                }
                adapter = new repoAdapter(cox, response.body());
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<DefaultResponse>> call, Throwable t) {
                Log.e("error_retrofit", String.valueOf(t.getMessage()));
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
                TotalItemCount = layoutManager.getItemCount();
                pastVisbleItem = layoutManager.findFirstVisibleItemPosition();
                if (dy > 0) {
                    if (isLoading) {
                        if (TotalItemCount > PreviousTotal) {
                            isLoading = false;
                            PreviousTotal = TotalItemCount;
                        }
                    }
                    if (!isLoading && (TotalItemCount - visibleItemCount) <= (pastVisbleItem + threshold)) {
                        page_number++;
                        Pagenation();
                        isLoading = true;
                    }
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                repoList.clear();
                adapter.notifyDataSetChanged();
                page_number = 1;
                isLoading = true;
                pastVisbleItem = 0;
                visibleItemCount = 0;
                TotalItemCount = 0;
                PreviousTotal = 0;
                threshold = 10;
                Call<List<DefaultResponse>> call = RetrofitClient.getInstance(cox).getApi()
                        .GitApi(String.valueOf(page_number),
                                String.valueOf(item_count),
                                Constants.Token);
                call.enqueue(new Callback<List<DefaultResponse>>() {
                    @Override
                    public void onResponse(Call<List<DefaultResponse>> call, Response<List<DefaultResponse>> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                repoList = response.body();
                            }
                        }
                        adapter = new repoAdapter(cox, response.body());
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<DefaultResponse>> call, Throwable t) {
                        Log.e("error_retrofit", String.valueOf(t.getMessage()));
                    }
                });
            }
        });
    }

    //make pagination 10 items per page
    private void Pagenation() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<DefaultResponse>> call = RetrofitClient.getInstance(cox).getApi()
                .GitApi(String.valueOf(page_number),
                        String.valueOf(item_count),
                        Constants.Token);
        call.enqueue(new Callback<List<DefaultResponse>>() {
            @Override
            public void onResponse(Call<List<DefaultResponse>> call, Response<List<DefaultResponse>> response) {

                if (response.isSuccessful()) {
                    if (!response.body().isEmpty()) {
                        List<DefaultResponse> repoListss = response.body();
                        adapter.addRepo(repoListss);
                        Toast.makeText(MainActivity.this, "Page" + page_number + " Is Loaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<DefaultResponse>> call, Throwable t) {
                Log.e("error_retrofit", String.valueOf(t.getMessage()));
            }
        });

    }
}