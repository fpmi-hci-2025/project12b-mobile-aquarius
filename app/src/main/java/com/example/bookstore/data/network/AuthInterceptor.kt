package com.example.bookstore.data.network

import com.example.bookstore.BuildConfig
import com.example.bookstore.data.api.AuthApiService
import com.example.bookstore.data.local.UserManager
import com.example.bookstore.data.model.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val userManager: UserManager
) : Interceptor {
    
    // Создаем отдельный Retrofit для refresh запросов (без AuthInterceptor)
    private val refreshRetrofit: Retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    private val refreshAuthApiService: AuthApiService by lazy {
        refreshRetrofit.create(AuthApiService::class.java)
    }
    
    @Volatile
    private var isRefreshing = false
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Получаем токен из UserManager
        val accessToken = userManager.getAccessToken()
        
        // Если токен есть, добавляем его в заголовок Authorization
        val newRequest = if (accessToken != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }
        
        // Выполняем запрос
        var response = chain.proceed(newRequest)
        
        // Если получили 401, пытаемся обновить токен
        if (response.code == 401 && accessToken != null) {
            synchronized(this) {
                // Проверяем, не обновляется ли уже токен
                if (!isRefreshing) {
                    isRefreshing = true
                    
                    try {
                        val refreshToken = userManager.getRefreshToken()
                        if (refreshToken != null) {
                            // Делаем запрос на refresh
                            val refreshResponse = runBlocking {
                                refreshAuthApiService.refresh(
                                    RefreshTokenRequest(
                                        accessToken = accessToken,
                                        refreshToken = refreshToken
                                    )
                                )
                            }
                            
                            // Сохраняем новые токены
                            userManager.updateTokens(
                                refreshResponse.accessToken,
                                refreshResponse.refreshToken
                            )
                            
                            // Повторяем оригинальный запрос с новым токеном
                            val newRequestWithToken = originalRequest.newBuilder()
                                .header("Authorization", "Bearer ${refreshResponse.accessToken}")
                                .build()
                            
                            response.close() // Закрываем старый response
                            response = chain.proceed(newRequestWithToken)
                        }
                    } catch (e: Exception) {
                        // Если refresh не удался, очищаем данные пользователя
                        userManager.clearUser()
                    } finally {
                        isRefreshing = false
                    }
                } else {
                    // Если токен уже обновляется, ждем и повторяем запрос
                    while (isRefreshing) {
                        Thread.sleep(100)
                    }
                    val newAccessToken = userManager.getAccessToken()
                    if (newAccessToken != null) {
                        val retryRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                        response.close()
                        response = chain.proceed(retryRequest)
                    }
                }
            }
        }
        
        return response
    }
}

