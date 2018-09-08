package com.hackathon.fellas.homedepotapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView


class NavigationActivity : AppCompatActivity() {

    var arrowImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        arrowImageView = findViewById(R.id.arrowImageView)
    }
}
