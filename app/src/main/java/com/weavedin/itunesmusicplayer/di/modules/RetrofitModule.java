package com.weavedin.itunesmusicplayer.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weavedin.itunesmusicplayer.di.modules.scope.PerActivityScope;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    public static final String BASE_API_URL = "http://itunes.apple.com/";
    public final static int CONNECTION_TIMEOUT = 30;
    private final static int READ_TIMEOUT = 30;
    private final static int WRITE_TIMEOUT = 30;

    private Gson gson = new GsonBuilder().create();

    private HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder requestBuilder = originalRequest.newBuilder();
                    return chain.proceed(requestBuilder.build());
                }
            })
            .addInterceptor(loggingInterceptor).build();

    @Provides
    @PerActivityScope
    public Retrofit retrofit() {


        OkHttpClient.Builder httpClientBuilder = okHttpClient.newBuilder();
        httpClientBuilder
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);

        return new Retrofit.Builder()
                .client(okHttpClient)
                .client(httpClientBuilder.build())
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
