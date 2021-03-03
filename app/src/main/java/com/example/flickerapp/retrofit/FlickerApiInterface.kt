package com.example.flickerapp.retrofit

import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickerApiInterface {

    @GET("rest")
    suspend fun getImages(
        @Query("per_page") perPage: String = "15",
        @Query("page") page: String,
        @Query("method") method: String,
        @Query("api_key") api: String,
        @Query("format") json: String,
        @Query("nojsoncallback") value: String,
        @Query("extras") extras: String
    ): Response<FlickerResponse>

    @GET("rest")
    suspend fun getSearchResult(
        @Query("per_page") perpage: String = "20",
        @Query("page") page: String,
        @Query("method") method: String,
        @Query("api_key") api: String,
        @Query("format") json: String,
        @Query("nojsoncallback") value: String,
        @Query("extras") extras: String,
        @Query("text") text: String
    ):Response<FlickerResponse>

}