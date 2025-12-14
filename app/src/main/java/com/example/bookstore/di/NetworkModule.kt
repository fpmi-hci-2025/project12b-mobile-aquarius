// NetworkModule.kt
package com.example.bookstore.di

import com.example.bookstore.BuildConfig
import com.example.bookstore.data.api.AuthApiService
import com.example.bookstore.data.api.CartApiService
import com.example.bookstore.data.api.OrderApiService
import com.example.bookstore.data.api.WishlistApiService
import com.example.bookstore.data.network.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // В debug режиме логируем все, в release - только ошибки
            level = if (BuildConfig.DEBUG) {
               HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // Добавляем AuthInterceptor перед logging
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // Используем BuildConfig
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Singleton
    @Provides
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
    
    @Singleton
    @Provides
    fun provideBookApiService(retrofit: Retrofit): com.example.bookstore.data.api.BookApiService {
        return retrofit.create(com.example.bookstore.data.api.BookApiService::class.java)
    }
    
    @Singleton
    @Provides
    fun provideCartApiService(retrofit: Retrofit): CartApiService {
        return retrofit.create(CartApiService::class.java)
    }
    
    @Singleton
    @Provides
    fun provideOrderApiService(retrofit: Retrofit): OrderApiService {
        return retrofit.create(OrderApiService::class.java)
    }
    
    @Singleton
    @Provides
    fun provideWishlistApiService(retrofit: Retrofit): WishlistApiService {
        return retrofit.create(WishlistApiService::class.java)
    }
}