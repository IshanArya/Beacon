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


class NavigationActivity : AppCompatActivity() {

    private var arrowImageView: ImageView? = null
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

        arrowImageView = findViewById(R.id.arrowImageView)
        arrowImageView?.setImageResource(R.drawable.arrow_forward)

        navigator.startNavigation()

        if (checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startBluetoothScan()
            Log.v("PERMS", "Bluetooth permission granted")
        } else {
            Log.e("PERMS", "Permission not granted for bluetooth")
        }
    }

    fun changeDirection(direction: Direction) {
        this.direction = direction
    }

    fun onButtonPressed(view: View) {
        var button = view as? Button
        val text = button?.getText().toString().toLowerCase()
        if (text == "forward") {
            arrowImageView?.setImageResource(R.drawable.arrow_forward)
        } else if (text == "left") {
            arrowImageView?.setImageResource(R.drawable.arrow_left)
        } else if (text == "right") {
            arrowImageView?.setImageResource(R.drawable.arrow_right)
        }
        Log.i("button text", button?.getText().toString())
    }

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private var navigator = NavigationMap { inst ->
        instructions.text = inst
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
