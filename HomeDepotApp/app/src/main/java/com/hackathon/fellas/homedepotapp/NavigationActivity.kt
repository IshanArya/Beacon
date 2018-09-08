package com.hackathon.fellas.homedepotapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.activity_navigation.returnHomeButton
import java.lang.Math.abs


class NavigationActivity : AppCompatActivity() {
    private var direction: Direction? = null

    enum class Direction {
        RIGHT, LEFT, FORWARD
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        returnHomeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        navigator.startNavigation()

        if (checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startBluetoothScan()
            Log.v("PERMS", "Bluetooth permission granted")
        } else {
            Log.e("PERMS", "Permission not granted for bluetooth")
        }
    }
    
    fun onButtonPressed(view: View) {
        var button = view as? Button
        Log.i("button text", button?.getText().toString())
    }

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private var navigator = NavigationMap { inst, degrees ->
        instructions.text = inst
        if (degrees != null) {
            if (abs(degrees) <= 15) {
                arrowImageView?.setImageResource(R.drawable.arrow_forward)
            } else if (degrees > 0) {
                arrowImageView?.setImageResource(R.drawable.arrow_left)
            } else if (degrees < 0) {
                arrowImageView?.setImageResource(R.drawable.arrow_right)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startBluetoothScan() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        bluetoothAdapter.startLeScan { device, signalStrength, scanRecord ->
            if (device.name != null && device.name.contains("Beacon")) {
                Log.v("BTLE: ", "Device ${device.name} with RSSI $signalStrength")
                val beaconId = Character.getNumericValue(device.name.get(device.name.indexOf("Beacon ") + "Beacon ".length)) - 1
                navigator.updateSignalReading(beaconId, signalStrength)
            }
        }
    }

}