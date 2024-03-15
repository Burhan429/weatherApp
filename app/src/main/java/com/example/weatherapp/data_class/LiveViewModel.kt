package com.example.weatherapp.data_class

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.Forecast
import com.example.weatherapp.WeatherList
import com.example.weatherapp.service.ApiInterface
import com.example.weatherapp.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveViewModel : ViewModel() {
    var factsLiveDataObject :MutableLiveData<WeatherList> = MutableLiveData()
    var factsLiveDataObject2 :MutableLiveData<Forecast> = MutableLiveData()

    private val apiInterface: ApiInterface

    init {
        val retrofit = RetrofitInstance.getRetrofitInstance()
        apiInterface = retrofit.create(ApiInterface::class.java)
    }
     fun fetchWeatherData(cityName: String) {
        apiInterface.getWeatherByCity(cityName, "9e61e3fff6a2f38704d2734d9619bbe8", "metric")
         .enqueue(object : Callback<WeatherList> {
            override fun onResponse(call: Call<WeatherList>, response: Response<WeatherList>) {
                if (response.isSuccessful) {
                    factsLiveDataObject.value = response.body()

                } else if (response.code()==404){
                    Log.e(TAG, "Response unsuccessful")

                }
                else
                    Log.e(TAG, "Response unsuccessful: ${response.message()}")
            }


            override fun onFailure(call: Call<WeatherList>, t: Throwable) {
                Log.e(TAG, "Failed to fetch weather data: ${t.message}")
            }
        })
    }


    fun fetchDailyWeather(cityName: String) {
        apiInterface.getDailyWeather(cityName, "9e61e3fff6a2f38704d2734d9619bbe8", "metric", 15)
            .enqueue(object : Callback<Forecast> {
                override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                    if (response.isSuccessful) {
                        factsLiveDataObject2.value = response.body()

                    } else if (response.code()==404){
                        Log.e(TAG, "Response unsuccessful")

                    }
                    else {
                        Log.e(TAG, "Response unsuccessful: ${response.message()}")

                    }
                }

                override fun onFailure(call: Call<Forecast>, t: Throwable) {
                    Log.e(TAG, "Failed to fetch daily weather data: ${t.message}")

                }
            })
    }
    companion object {
        private const val TAG = "LiveViewModel"
    }
}