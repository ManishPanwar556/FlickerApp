package com.example.flickerapp.retrofit


import com.squareup.moshi.Json

data class FlickerResponse(
    @Json(name = "photos")
    val photos: Photos,
    @Json(name = "stat")
    val stat: String
)