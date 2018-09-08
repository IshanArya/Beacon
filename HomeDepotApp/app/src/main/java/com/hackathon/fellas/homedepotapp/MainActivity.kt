package com.hackathon.fellas.homedepotapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helpButton.setOnClickListener {
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
        }

        listButton.setOnClickListener {
            val intent = Intent(this, Search::class.java)
            startActivity(intent)
        }

        directionButton.setOnClickListener {
            val intent = Intent(this, Navigation::class.java)
            startActivity(intent)
        }
    }
}
