package com.example.weatherapp.activity

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.CurrentAdapterClass
import com.example.weatherapp.adapter.DailyAdapterClass
import com.example.weatherapp.data_class.CurrentDataClass
import com.example.weatherapp.data_class.DailyDataClass
import com.example.weatherapp.data_class.LiveViewModel
import com.example.weatherapp.databinding.ActivityDesignBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DesignActivity : AppCompatActivity() {
    private lateinit var weatherViewModel: LiveViewModel
    private lateinit var adapter: CurrentAdapterClass
    private lateinit var adapter2: DailyAdapterClass

    private var dataList = ArrayList<CurrentDataClass>()
    private var dailyList = ArrayList<DailyDataClass>()
    private var selectedCity: String = "srinagar"
    private lateinit var binding: ActivityDesignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDesignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            // Restore the selected city from saved instance state
            selectedCity = savedInstanceState.getString(KEY_SELECTED_CITY, "srinagar") ?: "srinagar"
        }
        weatherViewModel = ViewModelProvider(this).get(LiveViewModel::class.java)

        weatherViewModel.fetchWeatherData(selectedCity)
        weatherViewModel.fetchDailyWeather(selectedCity)

        setupRecyclerView()
        searchCity()

        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            }
            false
        }

        weatherViewModel.factsLiveDataObject.observe(this) { weatherList ->
            weatherList?.let {
                val temperature = it.main?.temp.toString()
                val maxTemp = it.main?.tempMax.toString()
                val minTemp = it.main?.tempMin.toString()
                val condition = it.weather?.firstOrNull()?.main ?: "unknown"
                val cityName = it.name ?: "Srinagar"
                val humidity = it.main?.humidity.toString()
                val wind = it.wind?.speed.toString()
                val sunrise = time(it.sys?.sunrise ?: 0)
                val sunset = time(it.sys?.sunset ?: 0)
                val seaLevel = it.main?.seaLevel
                val weather = it.weather?.firstOrNull()?.main ?: "unknown"

                dataList.clear()
                dataList.add(
                    CurrentDataClass(
                        R.drawable.humidity,
                        "${it.main?.humidity} %",
                        "Humidity"
                    )
                )
                dataList.add(
                    CurrentDataClass(
                        R.drawable.wind,
                        "${it.wind?.speed} m/s",
                        "Wind Speed"
                    )
                )
                dataList.add(
                    CurrentDataClass(
                        R.drawable.climate_change,
                        it.weather?.firstOrNull()?.main ?: "unknown",
                        "Condition"
                    )
                )
                dataList.add(
                    CurrentDataClass(
                        R.drawable.sunrise,
                        "${time(it.sys?.sunrise ?: 0)}",
                        "Sunrise"
                    )
                )
                dataList.add(
                    CurrentDataClass(
                        R.drawable.sunset,
                        "${time(it.sys?.sunset ?: 0)}",
                        "Sunset"
                    )
                )
                dataList.add(
                    CurrentDataClass(
                        R.drawable.high_tide,
                        "${it.main?.seaLevel} hpa",
                        "Sea Level"
                    )
                )
                adapter.notifyDataSetChanged()

                binding.apply {
                    temp.text = "$temperature \u2103"
                    maxi.text = "MaxTemp: $maxTemp \u2103"
                    min.text = "MinTemp: $minTemp \u2103"
                    binding.condition.text = "$condition"
                    locationIcon.text = cityName
                    number1.text = "$humidity %"
                    number2.text = "$wind m/s"
                    number3.text = "$weather"
                    number4.text = "$sunrise"
                    number5.text = "$sunset"
                    number6.text = "$seaLevel hpa"

                    day.text = dayName(System.currentTimeMillis())
                    dating.text = date()
                    changeImagesAccordingToCondition(condition)
                }
            }
        }

        weatherViewModel.factsLiveDataObject2.observe(this) { forecast ->
            forecast?.let {
                dailyList.clear()
                setDate(Date())
                it.list?.forEachIndexed { index, forecastItem ->
                    val icon = forecastItem.weather?.getOrNull(0)?.icon ?: ""
                    val dailyData = DailyDataClass(
                        date(index),
                        getWeatherIconResource(icon),

                        forecastItem.main?.temp.toString() + " \u2103"
                    )

                    dailyList.add(dailyData)
                }
                adapter2.notifyDataSetChanged()

            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the selected city to the bundle
        outState.putString(KEY_SELECTED_CITY, selectedCity)
    }

    companion object {
        private const val KEY_SELECTED_CITY = "selected_city"
    }

    private fun setupRecyclerView() {
        binding.layout.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.layout.setHasFixedSize(true)
        adapter = CurrentAdapterClass(dataList)
        binding.layout.adapter = adapter

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.setHasFixedSize(true)
        adapter2 = DailyAdapterClass(dailyList)
        binding.recyclerView.adapter = adapter2
    }

    private fun searchCity() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    selectedCity = query
                    weatherViewModel.fetchWeatherData(query)
                    weatherViewModel.fetchDailyWeather(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun getWeatherIconResource(FlatIcon: String): Int {
        return when (FlatIcon) {
            "01d" -> R.drawable.clear_day
            "01n" -> R.drawable.clear_night
            "02d" -> R.drawable.cloudy_day
            "02n" -> R.drawable.cloudy_night
            "03d", "03n" -> R.drawable.scattered_clouds
            "04d", "04n" -> R.drawable.broken_clouds
            "09d", "09n" -> R.drawable.showers
            "10d", "10n" -> R.drawable.rainy
            "11d", "11n" -> R.drawable.thunderstorm
            "13d", "13n" -> R.drawable.snow
            "50d", "50n" -> R.drawable.mist
            else -> R.drawable.unknown
        }
    }

    private fun setDate(date: Date) {
        baseDate = date
    }

    private var baseDate: Date? = null

    private fun date(offset: Int): String {
        baseDate?.let { base ->
            val calendar = Calendar.getInstance()
            calendar.time = base
            calendar.add(Calendar.DATE, offset)
            val dateFormat = SimpleDateFormat("dd MMMM ", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }
        // Return empty string or handle error if baseDate is null
        return ""
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun time(timestamp: Int): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp.toLong() * 1000))
    }

    private fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun changeImagesAccordingToCondition(conditions: String) {
        when (conditions) {
            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimation.setAnimation(R.raw.sun)
            }

            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimation.setAnimation(R.raw.cloud)
            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimation.setAnimation(R.raw.rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimation.setAnimation(R.raw.snow)
            }

            else -> {
                binding.root.setBackgroundResource(R.drawable.clear_background)
            }
        }
        binding.lottieAnimation.playAnimation()
    }
}