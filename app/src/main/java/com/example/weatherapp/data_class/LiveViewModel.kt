package com.example.weatherapp.data_class

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.WeatherList
import com.example.weatherapp.service.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LiveViewModel : ViewModel() {
    var factsLiveDataObject :MutableLiveData<WeatherList> = MutableLiveData()


     fun fetchWeatherData(cityName: String) {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()

        val apiInterface = retrofit.create(ApiInterface::class.java)
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
    companion object {
        private const val TAG = "LiveViewModel"
    }
}