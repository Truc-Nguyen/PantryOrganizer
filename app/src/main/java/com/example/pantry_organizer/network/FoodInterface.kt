package com.example.pantry_organizer.network


import com.example.pantry_organizer.data.*

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodInterface {

    @Headers("x-app-id: 5705254a", "x-app-key: 76ea4edac97b177ffae9e7bb6c073c87")
    @GET("/v2/search/instant")
    ///trackapi.nutritionix.com/v2/search/instant?query=grilled cheese
    //headers:
    suspend fun getFoodBySearch(@Query("q") q: String): Response<FoodDataPayload>

}