package com.example.flickerapp.retrofit


import com.squareup.moshi.Json

data class Photo(
    @Json(name = "farm")
    val farm: Int,
    @Json(name = "height_s")
    val heightS: Int,
    @Json(name = "id")
    val id: String,
    @Json(name = "isfamily")
    val isfamily: Int,
    @Json(name = "isfriend")
    val isfriend: Int,
    @Json(name = "ispublic")
    val ispublic: Int,
    @Json(name = "owner")
    val owner: String,
    @Json(name = "secret")
    val secret: String,
    @Json(name = "server")
    val server: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "url_s")
    val url_s: String,
    @Json(name = "width_s")
    val widthS: Int
)