package ie.setu.bin_there_app.helpers

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap

class LocationHelper(private val activity: Activity) {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    var permissionDenied = false

    fun enableMyLocation(map: GoogleMap?) {
        map?.let {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                it.isMyLocationEnabled = true
            } else {
                // Request permissions
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray, map: GoogleMap?): Boolean {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation(map) // Make sure to call this after the permission is granted
                return true
            } else {
                permissionDenied = true
            }
        }
        return false
    }


    fun showCurrentLocationToast(location: android.location.Location) {
        Toast.makeText(activity, "Current location:\n Latitude: ${location.latitude}, Longitude: ${location.longitude}", Toast.LENGTH_LONG).show()
    }
}
