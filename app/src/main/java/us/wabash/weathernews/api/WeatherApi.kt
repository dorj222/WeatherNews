package us.wabash.weathernews.api

import us.wabash.weathernews.data.WeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi{

    @GET("/data/2.5/weather")
    fun getWeather(@Query("q") q: String, @Query("units") units: String = "metric", @Query("appid") appId: String = "59b06ea2e3a0e9bebb7fc3d6abb080e7") : Call<WeatherResult>
}