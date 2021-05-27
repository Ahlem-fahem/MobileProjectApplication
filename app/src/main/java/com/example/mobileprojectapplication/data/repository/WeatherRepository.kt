package com.example.mobileprojectapplication.data.repository



import com.example.mobileprojectapplication.data.api.ApiService
import com.example.mobileprojectapplication.data.models.State
import com.example.mobileprojectapplication.data.models.WeatherResult
import com.example.mobileprojectapplication.utils.NetworkOnlyResourceFlow
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class WeatherRepository() {
    private fun provideLogging() : HttpLoggingInterceptor{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }
    private fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.apply {
            connectTimeout(10, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            addInterceptor(provideLogging())
        }
        return okHttpClient.build()
    }
    var  apiService: ApiService = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(provideOkHttpClient())
        .build().create(ApiService::class.java)


    @ExperimentalCoroutinesApi
    fun getWeatherForCity(cityName: String, appId: String): Flow<State<WeatherResult>> {
        return object : NetworkOnlyResourceFlow<WeatherResult>() {

            override suspend fun createCall(): Response<WeatherResult> {
                return apiService.getWeatherForCity(cityName,appId)
            }

        }.asFlow()
    }
}