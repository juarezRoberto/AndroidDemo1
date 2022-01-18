package com.juarez.coppeldemo.api

import android.util.Log
import com.juarez.coppeldemo.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class HttpInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val requestBuilder = originalRequest
            .newBuilder()
            .addHeader("Authorization", "Token")
            .url(originalUrl.toString().replace("access-token", Constants.ACCESS_TOKEN))
            .build()
        return chain.proceed(requestBuilder)
    }
}