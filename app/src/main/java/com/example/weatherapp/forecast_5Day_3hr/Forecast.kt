package com.example.weatherapp

import com.example.weatherapp.forecast_5Day_3hr.List
import com.google.gson.annotations.SerializedName


data class Forecast (

  @SerializedName("cod"     ) var cod     : String?         = null,
  @SerializedName("message" ) var message : Int?            = null,
  @SerializedName("cnt"     ) var cnt     : Int?            = null,
  @SerializedName("list"    ) var list    : ArrayList<List> = arrayListOf(),
  @SerializedName("city"    ) var city    : City?           = City()

)