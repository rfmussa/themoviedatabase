package com.renato.moviedatabase.di

import com.renato.moviedatabase.api.ApiService
import com.renato.moviedatabase.schedulers.RuntimeSchedulerProvider
import com.renato.moviedatabase.schedulers.SchedulerProvider
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {


    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder()
        .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun providesOkHttpClient(authInterceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    fun providesRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit.Builder =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)

    @Provides
    fun providesApi(
        retrofit: Retrofit.Builder
    ): ApiService =
        retrofit.baseUrl("https://api.themoviedb.org/3/")
            .build()
            .create(ApiService::class.java)

    @Provides
    fun provideInterceptor(): Interceptor {
        return Interceptor {
            var request = it.request()

            val url = request.url()
                .newBuilder()
                .addQueryParameter("api_key", "369cfbe391bea3d53ae68abc59cfb6fc")
                .build()

            request = request.newBuilder()
                .url(url)
                .build()

            it.proceed(request)
        }
    }

    @Provides
    fun providesSchedulerProvider(): SchedulerProvider {
        return RuntimeSchedulerProvider()
    }
}