package com.example.pantry_organizer.api

import com.example.pantry_organizer.data.ApiFoodDataPayload
import com.example.pantry_organizer.data.ApiFoodNutritionPayload
import retrofit2.Response
import retrofit2.http.*

interface FoodInterface {

    // @Headers("x-app-id: 5705254a", "x-app-key: 76ea4edac97b177ffae9e7bb6c073c87")
    @Headers("x-app-id: 50719cb0", "x-app-key: 5c47de361f96f48bee05274d8d8d3eda")
    @GET("/v2/search/instant")
    suspend fun getFoodBySearch(@Query("query") query: String): Response<ApiFoodDataPayload>

    //@Headers("x-app-id: 5705254a", "x-app-key: 76ea4edac97b177ffae9e7bb6c073c87")
    @Headers("x-app-id: 50719cb0", "x-app-key: 5c47de361f96f48bee05274d8d8d3eda")
    @POST("/v2/natural/nutrients")
    @FormUrlEncoded
    suspend fun getFoodNutrients(@Field("query") query: String): Response<ApiFoodNutritionPayload>
}