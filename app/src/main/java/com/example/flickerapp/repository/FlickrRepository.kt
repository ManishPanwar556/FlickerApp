package com.example.flickerapp.repository

import com.example.flickerapp.utils.Utils
import com.example.flickerapp.retrofit.FlickerApiInterface
import com.example.flickerapp.retrofit.FlickerResponse
import com.example.flickerapp.retrofit.FlickerSearchApiInterface
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FlickrRepository {
    private lateinit var flickerApiInterface: FlickerApiInterface

    private var gson: Gson = GsonBuilder().create()

    init {
        flickerApiInterface =
            Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Utils.URL).build().create(FlickerApiInterface::class.java)

    }

    suspend fun getResponse(page:String,extra:String,apiKey: String, method: String): Response<FlickerResponse> {
        return flickerApiInterface.getImages(page=page,method = method, api=apiKey, json="json", value="1",extras=extra)
    }
    suspend fun getSearch(page:String,extra: String,apiKey: String,method: String,text:String):Response<FlickerResponse>{
        return flickerApiInterface.getSearchResult(page=page,method=method,api=apiKey,json="json",value="1",extras = extra,text=text)
    }
}