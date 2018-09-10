package com.hackathon.fellas.homedepotapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_navigation.*
import java.lang.Math.abs
import java.util.*


class NavigationActivity : AppCompatActivity(), SensorEventListener, TextToSpeech.OnInitListener {
    enum class Direction(val arrowString: String) {
        FORWARD("\u2b06\ufe0e"),
        DOWN("\u2b07\ufe0e"),
        LEFT("\u2b05\ufe0e"),
        RIGHT("\u27a1\ufe0e")
    }


    var stepsAtChange = 0
    var steps = 0
    private var direction: Direction = Direction.FORWARD

    lateinit var textToSpeech: TextToSpeech
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var navigator = NavigationMap { inst, degrees ->
        Log.v("CALLBACK INSTRUCTION", "Called with instructions -> $inst")
        instructions.text = inst
        if (degrees != null) {
            if (abs(degrees) <= 15) {
                arrowView.text = Direction.FORWARD.arrowString
            } else if (abs(degrees) > 165) {
                arrowView.text = Direction.DOWN.arrowString
            } else if (degrees > 0) {
                arrowView.text = Direction.LEFT.arrowString
                stepsAtChange = steps
                direction = Direction.LEFT
            } else if (degrees < 0) {
                arrowView.text = Direction.RIGHT.arrowString
                stepsAtChange = steps
                direction = Direction.RIGHT
            }
        }
        textToSpeech.speak(inst, TextToSpeech.QUEUE_ADD, null)
    }


    private val sensorManager: SensorManager by lazy { (getSystemService(Context.SENSOR_SERVICE) as SensorManager?)!! }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textToSpeech = TextToSpeech(this, this)
        setContentView(R.layout.activity_navigation)

        val bluetoothNode: BluetoothNode = intent.getSerializableExtra(SharedData.CHOSEN_NODE) as BluetoothNode

        var beaconId = when (bluetoothNode) {
            BluetoothNode.NODE1 -> 0
            BluetoothNode.NODE2 -> 1
            BluetoothNode.NODE3 -> 2
            else -> {
                0
            }
        }

        navigator.setGoal(beaconId)

        textToSpeech.setLanguage(Locale.CHINESE)

        navigator.startNavigation()
        val stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_GAME)

        if (checkSelfPermission(Manifest.permission.BLUETOOTH)
                == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startBluetoothScan()
            Log.v("PERMS", "Bluetooth permission granted")
        } else {
            Log.e("PERMS", "Permission not granted for bluetooth")
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.shutdown()
        sensorManager.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            steps = event.values[0].toInt()
            if (direction != Direction.FORWARD) {
                val stepsChange = steps - stepsAtChange
                if (stepsChange > 2) {
                    direction = Direction.FORWARD
                    arrowView.text = "\u2b06\ufe0e"
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startBluetoothScan() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        bluetoothAdapter.startLeScan { device, signalStrength, scanRecord ->
            if (device.name != null && device.name.contains("Beacon")) {
                val beaconId = Character.getNumericValue(
                        device.name.get(device.name.indexOf("Beacon ") + "Beacon ".length)
                ) - 1
                navigator.updateSignalReading(beaconId, signalStrength)
            }
        }
    }

    override fun onInit(p0: Int) {}
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

}