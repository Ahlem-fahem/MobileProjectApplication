package com.example.mobileprojectapplication.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileprojectapplication.models.City
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.mobileprojectapplication.R
import kotlinx.android.synthetic.main.nav_drawer_list_item.view.*


@ExperimentalCoroutinesApi
class CityListAdapter( private var cities: List<City>) : RecyclerView.Adapter<CityListAdapter.SingleViewHolder>() {
    var onItemCityDeleteClick: ((City) -> Unit)? = null
    var onItemClick: ((City) -> Unit)? = null

    fun setList(newCities: List<City>){
        cities = newCities
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SingleViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.nav_drawer_list_item, viewGroup, false)
        return SingleViewHolder(view)
    }

    override fun onBindViewHolder(singleViewHolder: SingleViewHolder, position: Int) {
        singleViewHolder.bind(cities[position])
    }

    override fun getItemCount(): Int {
        return cities.size
    }


    @ExperimentalCoroutinesApi
    inner class SingleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(city : City) {
            itemView.apply {
                title.text = city.name
                deleteButton.setOnClickListener {
                    onItemCityDeleteClick?.invoke(city)
                }
                setOnClickListener {
                    onItemClick?.invoke(city)
                }
            }

        }


    }





}