package edu.skku.map.capstone.util

import com.kakao.vectormap.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

fun calculateDistance(point1: LatLng, point2: LatLng): Double {
    val earthRadius = 6371 // Radius of the Earth in kilometers

    val lat1 = Math.toRadians(point1.latitude)
    val lon1 = Math.toRadians(point1.longitude)
    val lat2 = Math.toRadians(point2.latitude)
    val lon2 = Math.toRadians(point2.longitude)

    val dlon = lon2 - lon1
    val dlat = lat2 - lat1

    val a = sin(dlat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dlon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c // Distance in kilometers
}

fun getCafeDistance(user: LatLng, cafe: LatLng):String {
    val dist_ = calculateDistance(user, cafe)
    return (dist_ * 1000).roundToInt().toString()
}