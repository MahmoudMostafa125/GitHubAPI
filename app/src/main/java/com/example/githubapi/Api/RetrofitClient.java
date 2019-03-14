package com.example.githubapi.Api;

import android.content.Context;

import com.example.githubapi.Constants;
import com.example.githubapi.Utilities.internetConnection;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    static Context context;


    private RetrofitClient() {
        Long cacheSize = Long.valueOf(3 * 1024 * 1024); //size of cache 3 MB
        //File cacheDir = new File(context.getCacheDir(), "HttpCache");
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain)
                            throws IOException {
                        Request request = chain.request();
                        //check if connected to internet
                        if (!internetConnection.internetConnectionAvailable(1000)) {
                            //cache data for 28 days
                            int maxStale = 60 * 60 * 24 * 28;
                            request = request
                                    .newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .build();
                        }
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.mainLink)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static synchronized RetrofitClient getInstance(Context cox) {
        context = cox;
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    //make instant from retrofit
    public Api getApi() {
        return retrofit.create(Api.class);
    }

}

