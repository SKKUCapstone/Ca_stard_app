package edu.skku.map.capstone

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.TrackingManager
import edu.skku.map.capstone.databinding.FragmentHomeBinding

class HomeFragment() : Fragment() {
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationManager:LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var labelManager:LabelManager
    private lateinit var trackingManager:TrackingManager
    private var myPosition:Label? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setLocationManager()
        setListener()
        setLocationListener()


        binding.kakaoMV.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception) {
                Log.d("@@@ kakao map error", error.toString())
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("@@@ kakao map ready", "!")
                labelManager = kakaoMap.labelManager!!
                trackingManager = kakaoMap.trackingManager!!
            }
        })

        return binding.root
    }


    private fun setLocationManager(){
        Log.d("gps", "initializeLocationObj")
        if(::locationManager.isInitialized.not()) {
            locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        }
    }
    private fun setLocationListener() {
        if(::locationListener.isInitialized.not()) {
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    Log.d("gps", "onLocationChanged")
                    var lat = 0.0
                    var lng = 0.0
                    if (location != null) {
                        lat = location.latitude
                        lng = location.longitude
                        Log.d("gps", "lat: $lat, lng: $lng")
                    }
                    if(myPosition == null){
                        myPosition = createLabel(lat, lng)
                        trackingManager.startTracking(createLabel(lat,lng))
                    }
                    else{
                        myPosition!!.moveTo(LatLng.from(lat,lng))
                    }
                    Log.d("gps", "label: $myPosition")
                }

                override fun onProviderEnabled(provider: String) {
                    Log.d("gps", "onProviderEnabled")
                }

                override fun onProviderDisabled(provider: String) {}
            }
        }


    }
    private fun getCurrentLocation() {
        Log.d("gps", "getCurrentLocation")
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("gps","requestLocationUpdates")
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1500L,
                30f,
                locationListener
            )

        }
        else{
            Log.d("gps","permission needed")
        }

    }
    private fun setListener() {
        binding.gpsBtn.setOnClickListener {
            getCurrentLocation()
        }
    }

    private fun createLabel(lat: Double, lon: Double): Label {
        val styles: LabelStyles =
            labelManager.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.mymarker)))!!
        val options: LabelOptions = LabelOptions.from(LatLng.from(lat, lon)).setStyles(styles)
        val layer: LabelLayer = labelManager.layer!!
        return layer.addLabel(options)
    }



}