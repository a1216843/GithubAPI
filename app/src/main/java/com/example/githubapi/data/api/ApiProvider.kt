package com.example.githubapi.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiProvider {
    private const val baseUrl = "https://api.github.com/"
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getOkhttpClient())
            // 받은 응답을 Observable 형태로 변환함
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(getGsonConverter())
        .build()
    val UserApi = retrofit.create(UserApi::class.java)
    val RepoApi = retrofit.create(RepoApi::class.java)

    private fun getGsonConverter() = GsonConverterFactory.create()

    private fun getOkhttpClient() = OkHttpClient.Builder().apply {
        // timeout 시간을 지정
        readTimeout(60, TimeUnit.SECONDS)
        connectTimeout(60, TimeUnit.SECONDS)
        writeTimeout(5, TimeUnit.SECONDS)

        // 오고가는 네트워크 요청/응답을 로그로 표시
        addInterceptor(getLoggingInterceptor())
    }.build()

    private fun getLoggingInterceptor() : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}