package us.wabash.weathernews

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_weather_info.*
import kotlinx.android.synthetic.main.city_row.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import us.wabash.weathernews.GoogleMaps.MapsActivity
import us.wabash.weathernews.api.WeatherApi
import us.wabash.weathernews.data.WeatherResult
import java.text.SimpleDateFormat
import java.util.*
import android.R.id.edit
import android.content.SharedPreferences
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class WeatherInfo : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    var city = ""
    var timezone = 0
    var prs = "Pressure: "
    var hmd = "Humidity: "
    var spce = " mb    "


    companion object{

      var latCity = 0.0

      var lonCity = 0.0
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_info)

        var intent = intent
        val cityString = intent.getStringExtra("cityName")

        city = cityString + ""


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherAPI = retrofit.create(WeatherApi::class.java)
        callAPI(weatherAPI, cityString)

        btnDone.setOnClickListener {
            super.onBackPressed()
        }


    }

    private fun callAPI(weatherAPI: WeatherApi, city: String?) {



        val weatherCall = weatherAPI.getWeather(city!!)

        weatherCall.enqueue(object : Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                Toast.makeText(this@WeatherInfo, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(

                call: Call<WeatherResult>,
                response: Response<WeatherResult>
            ) {


                val wtrRst = response.body()

                if (wtrRst != null) {

                    createMapFragment()

                    setWeatherInfo(wtrRst, city)
                } else {
                    tvCityTemp.text = "No information is available"
                    Toast.makeText(
                        this@WeatherInfo,
                        "We are sorry! We don't have any weather news of " + city, Toast.LENGTH_LONG
                    ).show()
                }
            }

        })



    }

    private fun createMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setWeatherInfo(wtrRst: WeatherResult, city: String): Double{
        timezone = wtrRst.timezone.toString().toInt() - 3600



        val sdf = SimpleDateFormat("HH:mm:ss")
        val netDate = Date((wtrRst?.sys?.sunrise?.toString()!!.toLong() + timezone) * 1000)
        val netDate2 = Date((wtrRst?.sys?.sunset?.toString()!!.toLong() + timezone) * 1000)
        tvCity.text = city + ", " + wtrRst?.sys?.country?.toString()

        tvCityTemp.text =
            wtrRst?.main?.temp?.toString() + "°C " + "\n" + wtrRst?.weather?.get(0)?.description

        tvSunset.text = "Sunrise at " + sdf.format(netDate) + "   Sunset at " + sdf.format(netDate2)

        tvMinMax.text =
            "Daily High: " + wtrRst?.main?.temp_max.toString() + "°C      Daily Low: " + wtrRst.main?.temp_min + "°C"
        tvDesc.text =
            prs + wtrRst?.main?.pressure?.toString() + spce + hmd + wtrRst.main?.humidity?.toString() + "%"

        Glide.with(this@WeatherInfo).load(
            "https://openweathermap.org/img/w/" +
                    wtrRst?.weather?.get(0)?.icon
                    + ".png"
        ).into(weatherImage)

        val strLat = wtrRst?.coord?.lon!!.toDouble()
        val strLon = wtrRst?.coord?.lat!!.toDouble()

        latCity = strLat;
        lonCity = strLon;


        return latCity;
        return lonCity;

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        var city = LatLng(lonCity, latCity)

        mMap.addMarker(MarkerOptions().position(city).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(city))
    }

}
