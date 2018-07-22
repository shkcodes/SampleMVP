package com.shkmishra.samplemvp.di

import com.shkmishra.samplemvp.data.api.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    private val baseUrl = "https://api.foursquare.com"

    @Singleton
    @Provides
    fun provideOkhttp(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor { chain ->
            val original = chain.request()
            val url = chain.request().url().newBuilder()
                    .addQueryParameter("client_id", "WTAN224S3NOSIBJXANZVNHIPS5KMMETPLEUYNEL0AM4XO5SI")
                    .addQueryParameter("client_secret", "PHEP002AYWMHHSWKL3ZULZ3M0T03MRC2420C0GT4RUZQ2UXE")
                    .addQueryParameter("v", "20180609")
                    .build()
            chain.proceed(original.newBuilder().url(url).build())
        }
        client.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return client.build()
    }


    @Singleton
    @Provides
    fun provideApiService(client: OkHttpClient): ApiService {
        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        return builder.create(ApiService::class.java)
    }
}