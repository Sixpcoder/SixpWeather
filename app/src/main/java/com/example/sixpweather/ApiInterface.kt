package com.example.sixpweather


import com.example.sixpweather.api.Weather
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather")
    fun getweatherdata(

        @Query("q") city:String,
        @Query("appid") appid:String,
        @Query("units") units:String
    ) : Call<Weather>
}