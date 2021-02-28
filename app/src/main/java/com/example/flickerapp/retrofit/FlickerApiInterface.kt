package com.example.flickerapp.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickerApiInterface {

    @GET("rest")
    suspend fun getImages(
        @Query("per_page")perPage:String="20",
        @Query("method") method: String,
        @Query("api_key") api: String,
        @Query("format") json: String,
        @Query("nojsoncallback") value: String,
        @Query("extras") extras:String
    ): Response<FlickerResponse>

}