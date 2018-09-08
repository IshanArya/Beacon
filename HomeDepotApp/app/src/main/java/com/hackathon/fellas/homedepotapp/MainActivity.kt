package com.hackathon.fellas.homedepotapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun startSearch(view: View) {
        val intent: Intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}
