package com.naatvtelugu.news.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

data class LocationItem(
    val name: String,
    val district: String,
    val state: String,
) {
    val displayLabel: String get() = "$name, $district"
}

object LocationHelper {

    val popularLocations = listOf(
        LocationItem("పొన్నమండ (Ponnamanda)", "డా. బి.ఆర్. అంబేద్కర్ కోనసీమ", "ఆంధ్రప్రదేశ్"),
        LocationItem("రాజోలు (Razole)", "కోనసీమ జిల్లా", "ఆంధ్రప్రదేశ్"),
        LocationItem("అమలాపురం (Amalapuram)", "కోనసీమ జిల్లా", "ఆంధ్రప్రదేశ్"),
        LocationItem("కాకినాడ (Kakinada)", "కాకినాడ జిల్లా", "ఆంధ్రప్రదేశ్"),
        LocationItem("రాజమండ్రి (Rajahmundry)", "తూర్పు గోదావరి", "ఆంధ్రప్రదేశ్"),
        LocationItem("విజయవాడ (Vijayawada)", "ఎన్టీఆర్ జిల్లా", "ఆంధ్రప్రదేశ్"),
        LocationItem("గుంటూరు (Guntur)", "గుంటూరు జిల్లా", "ఆంధ్రప్రదేశ్"),
        LocationItem("విశాఖపట్నం (Visakhapatnam)", "విశాఖ జిల్లా", "ఆంధ్రప్రదేశ్"),
        LocationItem("తిరుపతి (Tirupati)", "తిరుపతి జిల్లా", "ఆంధ్రప్రదేశ్"),
        LocationItem("నెల్లూరు (Nellore)", "నెల్లూరు జిల్లా", "ఆంధ్రప్రదేశ్"),
        LocationItem("కర్నూలు (Kurnool)", "కర్నూలు జిల్లా", "ఆంధ్రప్రదేశ్"),
        LocationItem("అనంతపురం (Anantapur)", "అనంతపురం జిల్లా", "ఆంధ్రప్రదేశ్"),
        LocationItem("హైదరాబాద్ (Hyderabad)", "హైదరాబాద్", "తెలంగాణ"),
        LocationItem("వరంగల్ (Warangal)", "వరంగల్ జిల్లా", "తెలంగాణ"),
        LocationItem("ఖమ్మం (Khammam)", "ఖమ్మం జిల్లా", "తెలంగాణ"),
        LocationItem("కరీంనగర్ (Karimnagar)", "కరీంనగర్ జిల్లా", "తెలంగాణ"),
        LocationItem("నిజామాబాద్ (Nizamabad)", "నిజామాబాద్ జిల్లా", "తెలంగాణ"),
    )

    @SuppressLint("MissingPermission")
    suspend fun detectGpsLocation(context: Context): LocationItem? = withContext(Dispatchers.IO) {
        try {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return@withContext null
            val isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            var lastKnown: Location? = null
            if (isGpsEnabled) {
                lastKnown = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            if (lastKnown == null && isNetworkEnabled) {
                lastKnown = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            if (lastKnown != null) {
                val geocoder = Geocoder(context, Locale("te", "IN"))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val addresses = geocoder.getFromLocation(lastKnown.latitude, lastKnown.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val addr = addresses[0]
                        val city = addr.subLocality ?: addr.locality ?: addr.subAdminArea ?: "ఆంధ్రప్రదేశ్"
                        val district = addr.subAdminArea ?: addr.adminArea ?: "కోనసీమ"
                        val state = addr.adminArea ?: "ఆంధ్రప్రదేశ్"
                        return@withContext LocationItem(city, district, state)
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(lastKnown.latitude, lastKnown.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val addr = addresses[0]
                        val city = addr.subLocality ?: addr.locality ?: addr.subAdminArea ?: "ఆంధ్రప్రదేశ్"
                        val district = addr.subAdminArea ?: addr.adminArea ?: "కోనసీమ"
                        val state = addr.adminArea ?: "ఆంధ్రప్రదేశ్"
                        return@withContext LocationItem(city, district, state)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }
}
