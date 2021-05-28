package com.example.mobileprojectapplication.ui.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mobileprojectapplication.R
import com.example.mobileprojectapplication.models.City
import com.example.mobileprojectapplication.models.NetworkErrorType
import com.example.mobileprojectapplication.models.State
import com.example.mobileprojectapplication.models.WeatherResult
import com.example.mobileprojectapplication.ui.home.viewModel.WeatherViewModel
import com.example.mobileprojectapplication.utils.hideLoadingView
import com.example.mobileprojectapplication.utils.isOnline
import com.example.mobileprojectapplication.utils.showErrorView
import com.example.mobileprojectapplication.utils.showLoadingView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


class HomeFragment : Fragment(){

    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        weatherViewModel =
                ViewModelProvider(this).get(WeatherViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadFragment()
    }

    private fun loadFragment() {
        appLoader.showLoadingView()
        val city = arguments?.get("city") as City
        weatherViewModel.getWeatherForCity(city.name, getString(R.string.android_project_api))
        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is State.Loading -> {
                }
                is State.Success -> {
                    loadView(result.data)
                    appLoader.hideLoadingView()

                }
                is State.Error -> {
                    (requireActivity().isOnline({
                        appLoader?.showErrorView(NetworkErrorType.SERVER)
                    }) {
                        appLoader?.showErrorView(NetworkErrorType.NETWORK) { loadFragment() }
                    })
                }
            }

        })
    }

    private fun loadView(weatherResult: WeatherResult){
        val icon = weatherResult.weather[0].icon
        val description = weatherResult.weather[0].description

        //get icon from open weather map api
        val iconUrl = "http://openweathermap.org/img/w/$icon.png"
        Picasso.get().load(iconUrl).into(weatherIcon, object : Callback {
            override fun onSuccess() {
                Log.d("LOAD_IMAGE_SUCCESS", iconUrl)
            }

            override fun onError(e: Exception?) {
                Log.d("LOAD_IMAGE_ERROR", e?.message.toString())
            }
        })

        view?.background = getBackground(icon)

        weatherDescription.text = description
        cityNameTv.text = weatherResult.name
        minTempTv.text = "${weatherResult.main.temp_min.toInt()} °C"
        maxTempTv.text = "${weatherResult.main.temp_max.toInt()} °C"
        pressureTv.text = "${weatherResult.main.pressure}"
        humidityTv.text = "${weatherResult.main.humidity}%"
        currentTempTv.text = "${weatherResult.main.temp.toInt()} °C"


    }

    //display background image according to weather condition -> https://openweathermap.org/weather-conditions
    private fun getBackground(icon: String): Drawable? {
        return if (icon.contains("n")) {
            ContextCompat.getDrawable(requireContext(), R.drawable.night_background)
        } else {
            if (icon.contains("01") || icon.contains("02") || icon.contains(
                    "03"
                )
            ) {
                ContextCompat.getDrawable(requireContext(), R.drawable.sun_background)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.run_background)
            }
        }
    }
}