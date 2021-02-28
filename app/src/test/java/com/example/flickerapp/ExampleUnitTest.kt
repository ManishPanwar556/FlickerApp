package com.example.flickerapp

import com.example.flickerapp.retrofit.FlickerApiInterface
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private lateinit var retrofit: FlickerApiInterface
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        gson = GsonBuilder().create()
        retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.flickr.com/services/").build()
            .create(FlickerApiInterface::class.java)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun testApi(){
        runBlocking {
            val res=retrofit.getImages("flickr.photos.getRecent","6f102c62f41998d151e5a1b48713cf13","json","1","url_s")
            assertNotNull(res.body()?.photos?.photo?.get(0)?.url_s)
        }

    }


}