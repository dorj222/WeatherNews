package us.wabash.weathernews.GoogleMaps

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import us.wabash.weathernews.R
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.content.Context


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var lonCityString = ""
    var latCityString = ""

    var lonDouble = 0.0
    var latDouble = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(us.wabash.weathernews.R.layout.activity_weather_info)
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(us.wabash.weathernews.R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//
//
//        val mySharedPreferences =
//            this.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
//        val lonCity = mySharedPreferences.getString("lonCity", "")
//        val latCity = mySharedPreferences.getString("latCity", "")
//
//        lonCityString = lonCity + ""
//        latCityString = latCity + ""
//
//        lonDouble = lonCityString.toDouble()
//        latDouble = latCityString.toDouble()

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val city = LatLng(47.0, 19.0)
//        mMap.addMarker(MarkerOptions().position(city).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(city))
    }
}

