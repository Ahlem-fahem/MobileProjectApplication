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
import com.example.mobileprojectapplication.data.models.City
import com.example.mobileprojectapplication.data.models.NetworkErrorType
import com.example.mobileprojectapplication.data.models.State
import com.example.mobileprojectapplication.data.models.WeatherResult
import com.example.mobileprojectapplication.ui.home.viewModel.WeatherViewModel
import com.example.mobileprojectapplication.utils.hideLoadingView
import com.example.mobileprojectapplication.utils.isOnline
import com.example.mobileprojectapplication.utils.showErrorView
import com.example.mobileprojectapplication.utils.showLoadingView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


class EmptyFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         return inflater.inflate(R.layout.fragment_empty, container, false)
    }

}