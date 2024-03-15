package com.example.weatherapp.forecast_5Day_3hr

import com.google.gson.annotations.SerializedName


data class Coord (

  @SerializedName("lat" ) var lat : Double? = null,
  @SerializedName("lon" ) var lon : Double? = null

)