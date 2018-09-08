package com.hackathon.fellas.homedepotapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.os.Build
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private var navigator = NavigationMap { inst ->
        instructions.text = inst
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 123
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helpButton.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }

        listButton.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        navigator.startNavigation()

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)

        if (checkSelfPermission(Manifest.permission.BLUETOOTH) == PermissionChecker.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            startBluetoothScan()
            Log.v("PERMS", "Bluetooth permission granted")
        } else {
            Log.e("PERMS", "Permission not granted for bluetooth")
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            startBluetoothScan()
        }
    }
}
