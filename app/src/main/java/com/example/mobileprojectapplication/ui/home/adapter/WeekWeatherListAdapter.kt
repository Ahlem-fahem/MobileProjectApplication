package com.example.mobileprojectapplication.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileprojectapplication.models.City
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.mobileprojectapplication.R
import com.example.mobileprojectapplication.models.weekWeather.Days
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.rv_next_day_weather_item.view.*


@ExperimentalCoroutinesApi
class WeekWeatherListAdapter(private var weekWeather: List<Days>) : RecyclerView.Adapter<WeekWeatherListAdapter.SingleViewHolder>() {


    fun setList(newList: List<Days>){
        weekWeather = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SingleViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_next_day_weather_item, viewGroup, false)
        return SingleViewHolder(view)
    }

    override fun onBindViewHolder(singleViewHolder: SingleViewHolder, position: Int) {
        singleViewHolder.bind(weekWeather[position])
    }

    override fun getItemCount(): Int {
        return weekWeather.size
    }


    @ExperimentalCoroutinesApi
    inner class SingleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(day : Days) {
            itemView.apply {
                val icon = day.weather[0].icon
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
                weatherIcon
                maxMinTempTv.text = "${day.temp.max.toInt()} °C / ${day.temp.min.toInt()} °C "

            }

        }


    }





}