package com.example.sixpweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import com.example.sixpweather.api.Main
import com.example.sixpweather.api.Weather
import com.example.sixpweather.databinding.ActivityMainBinding
import com.example.sixpweather.databinding.ActivitySplashBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//02b3851f38be5e30aa1681f80b761337

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchweatherdata("Lucknow")
        searchcity()
    }

    private fun searchcity() {
        val searchview = binding.searchView
        searchview.setOnQueryTextListener(object :SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    fetchweatherdata(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchweatherdata(cityname:String) {
        val retrofit=Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(ApiInterface::class.java)

        val response =retrofit.getweatherdata(cityname,"02b3851f38be5e30aa1681f80b761337","metric")
        response.enqueue(object :Callback<Weather>{


            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                val responsebody = response.body()

                if (response.isSuccessful && responsebody !=null) {

                    val temperature = responsebody.main.temp.toString()
                    val wind = responsebody.wind.speed.toString()
                    val humidity = responsebody.main.humidity.toString()
                    val sunRise = responsebody.sys.sunrise.toString().toLong()
                    val sunset = responsebody.sys.sunset.toString().toLong()
                    val tempmax = responsebody.main.temp_max.toString()
                    val tempmin = responsebody.main.temp_min.toString()
                    val sealev = responsebody.main.pressure.toString()
                    val condition= responsebody.weather.firstOrNull()?.main?:"unknown"


                    binding.temp.text = "$temperature °C"
                    binding.max.text= "Max $tempmax °C"
                    binding.min.text= "Min $tempmin °C"
                    binding.humidity.text = "$humidity %"
                    binding.wind.text ="$wind m/s"
                    binding.sunset.text = "${time(sunset)} "
                    binding.tvsunrise.text = "${time(sunRise)}"
                    binding.sea.text="$sealev hPa"
                    binding.weather.text = condition
                    binding.tvconditions.text=condition
                    binding.day.text = dayname(System.currentTimeMillis())
                    binding.day.text = date()
                    binding.Cityname.text = "  $cityname"

                    changeimageacctoweathercondition(condition)

                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
            }

        })

    }

    private fun changeimageacctoweathercondition(condition :String) {
        when(condition){
            "Haze","Mist","Foggy","Clouds" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Sunny" ,"Clear","Clear Sky"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Light Snow","Blizzard","Heavy Snow" ->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            "Light Rain","Drizzle","Heavy Rain","Showers" ->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)

            }


        }
        binding.lottieAnimationView.playAnimation()

    }

    fun dayname(timestamp:Long):String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }

    fun date():String{
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    fun time(timestamp: Long):String{
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp*1000))

    }

}