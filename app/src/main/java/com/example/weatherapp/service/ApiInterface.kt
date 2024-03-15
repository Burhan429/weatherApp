package com.example.weatherapp.service


import com.example.weatherapp.Forecast
import com.example.weatherapp.WeatherList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("forecast?")
    fun getDailyWeather(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("cnt") cnt: Int
    ): Call<Forecast>

    @GET("weather?")
    fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Call<WeatherList>
}