package com.dvinov.recipes.screens.recipes.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Объект с реализацией RecipeApi (хранятся запросы) для взаимодействия с сервером
 */

object RecipeNetworkManager {
    private const val BASE_URL = "https://hf-android-app.s3-eu-west-1.amazonaws.com/"

    fun getRecipeApi(): RecipeApi{
        val appHttpClient: OkHttpClient by lazy {
            OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()
        }

        val api: RecipeApi by lazy {
            Retrofit.Builder()
                .client(appHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(RecipeApi::class.java)
        }

        return api
    }
}