package com.example.weatherapp.current_weather_data

import com.google.gson.annotations.SerializedName


data class Sys (

  @SerializedName("country" ) var country : String? = null,
  @SerializedName("sunrise" ) var sunrise : Int?    = null,
  @SerializedName("sunset"  ) var sunset  : Int?    = null,
  @SerializedName("pod" ) var pod : String? = null,

)