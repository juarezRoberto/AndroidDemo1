package com.juarez.coppeldemo.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.juarez.coppeldemo.api.HeroAPI
import com.juarez.coppeldemo.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun providesHttpInterceptor(): Interceptor {
        return Interceptor {
            val originalRequest = it.request()
            val originalUrl = originalRequest.url

            val requestBuilder = originalRequest
                .newBuilder()
                .addHeader("Authorization", "Token")
                .url(originalUrl.toString().replace("access-token", Constants.ACCESS_TOKEN))
                .build()
            it.proceed(requestBuilder)
        }
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    fun provideHttpClient(
        httpInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideHeroAPI(retrofit: Retrofit): HeroAPI = retrofit.create(HeroAPI::class.java)
}