package com.example.weatherapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.remote.ForecastWeatherResponse
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.other.Resource
import com.example.weatherapp.repositories.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel @ViewModelInject constructor(
    private val repository: WeatherRepository
): ViewModel(){


    private val _currentWeather = MutableLiveData<Resource<WeatherResponse>>()
    val currentWeather: LiveData<Resource<WeatherResponse>> = _currentWeather

    private val _searchWeather = MutableLiveData<Resource<WeatherResponse>>()
    val searchWeather: LiveData<Resource<WeatherResponse>> = _searchWeather

    private val _forecastWeather = MutableLiveData<Resource<ForecastWeatherResponse>>()
    val forecastWeather: LiveData<Resource<ForecastWeatherResponse>> = _forecastWeather

    fun getCurrentWeather(lat: Double,lon: Double) = viewModelScope.launch {
        _currentWeather.value = Resource.loading(null)
        val response = repository.getCurrentWeather(lat,lon)
        _currentWeather.value = response
    }

    fun searchForWeather(location: String) = viewModelScope.launch {
        _searchWeather.value = Resource.loading(null)
        val response = repository.searchForWeather(location)
        _searchWeather.value = response
    }

    fun forecastWeather(lat: Double,lon: Double) = viewModelScope.launch {
        _forecastWeather.value = Resource.loading(null)
        val response = repository.forecastWeather(lat,lon)
        _forecastWeather.value = response
    }
}