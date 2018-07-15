package com.weather.di.module;

import com.weather.BuildConfig;
import com.weather.data.network.ApiService;
import com.weather.data.network.IApiService;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHTTPClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl finalUrl = originalHttpUrl.newBuilder()
                        .addQueryParameter("appid", BuildConfig.API_KEY)
                        .addQueryParameter("units", "metric") // show temp. in celsius
                        .build();


                Request request = chain.request()
                        .newBuilder()
                        .url(finalUrl)
                        .build();
                return chain.proceed(request);
            }
        });

        return httpClient.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofitClient(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    IApiService provideApiService(ApiService apiService) {
        return apiService;
    }
}
