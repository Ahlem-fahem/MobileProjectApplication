package com.example.mobileprojectapplication.data.api

import com.example.mobileprojectapplication.models.WeatherResult
import com.example.mobileprojectapplication.models.weekWeather.WeekWeatherResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather?")
    suspend fun getWeatherForCity(@Query("q") cityName: String,@Query("appid") appId: String,@Query("units") units: String="metric",@Query("lang") lan: String="fr"):  Response<WeatherResult>

    @GET("forecast/daily?")
    suspend fun getWeatherCityForNextDays(@Query("q") cityName: String,@Query("appid") appId: String,@Query("units") units: String="metric",@Query("lang") lan: String="fr",@Query("cnt") cnt: Int=7):  Response<WeekWeatherResult>
}
