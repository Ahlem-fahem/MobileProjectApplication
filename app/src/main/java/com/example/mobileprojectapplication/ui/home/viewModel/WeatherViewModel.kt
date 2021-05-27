package com.example.mobileprojectapplication.ui.home.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mobileprojectapplication.models.State
import com.example.mobileprojectapplication.models.WeatherResult
import com.example.mobileprojectapplication.data.repository.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class WeatherViewModel(application: Application) :
    AndroidViewModel(application) {
    private val weatherRepository = WeatherRepository()

    private val _weatherLiveData = MutableLiveData<State<WeatherResult>>()
    val weatherLiveData : LiveData<State<WeatherResult>>
        get() = _weatherLiveData

    @ExperimentalCoroutinesApi
    fun getWeatherForCity(cityName: String, appId: String) {
        viewModelScope.launch {
            weatherRepository.getWeatherForCity(cityName, appId).collect {
                _weatherLiveData.value = it
                if (it is State.Success) {
                    this.cancel()
                }
            }
        }
    }

}