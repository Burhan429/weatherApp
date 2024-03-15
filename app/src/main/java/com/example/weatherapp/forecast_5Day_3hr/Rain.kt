package com.example.weatherapp.forecast_5Day_3hr

import com.google.gson.annotations.SerializedName


data class Rain (

  @SerializedName("3h" ) var rain3h : Double? = null

)