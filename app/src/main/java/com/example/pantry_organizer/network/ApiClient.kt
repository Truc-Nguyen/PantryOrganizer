package com.example.pantry_organizer.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    const val BASE_URL = "https://trackapi.nutritionix.com/v2/"

    fun makeRetrofitService(): FoodInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(FoodInterface::class.java)
    }
}