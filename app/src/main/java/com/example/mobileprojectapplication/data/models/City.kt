package com.example.mobileprojectapplication.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class City (@PrimaryKey val name : String,val isActualCity: Boolean = false) : Parcelable