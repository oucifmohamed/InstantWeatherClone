package com.example.weatherapp.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.weatherapp.data.remote.ForecastWeatherResponse
import com.example.weatherapp.databinding.WeatherItemBinding
import com.example.weatherapp.other.WeatherIconGenerator
import kotlin.math.roundToInt

class ForecastWeatherRecyclerViewAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ForecastWeatherResponse.WeatherForecast>() {

        override fun areItemsTheSame(
            oldItem: ForecastWeatherResponse.WeatherForecast,
            newItem: ForecastWeatherResponse.WeatherForecast
        ): Boolean {
            return newItem.dt == oldItem.dt
        }

        override fun areContentsTheSame(
            oldItem: ForecastWeatherResponse.WeatherForecast,
            newItem: ForecastWeatherResponse.WeatherForecast
        ): Boolean {
            return newItem == oldItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: WeatherItemBinding = WeatherItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ForecastWeatherViewHolder(
            binding,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ForecastWeatherViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ForecastWeatherResponse.WeatherForecast>) {
        differ.submitList(list)
    }

    class ForecastWeatherViewHolder
    (
        private val binding: WeatherItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: ForecastWeatherResponse.WeatherForecast) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            binding.mainWeather.text = item.weatherDescriptions.first().main
            binding.weatherDescription.text = item.weatherDescriptions.first().description
            binding.weatherTemperature.text = "${item.main.temp_max.minus(273.15).roundToInt()}\u2103"
            binding.humidityValue.text = "${item.main.humidity}%"
            binding.windSpeedValue.text = "${item.wind.speed} m/s"
            binding.date.text = item.dt_txt

            WeatherIconGenerator.getIconResources(context,binding.weatherIconView,item.weatherDescriptions.first().description)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ForecastWeatherResponse.WeatherForecast)
    }
}