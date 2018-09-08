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

    fun goToMyList(view: View) {
        val intent: Intent = Intent(this, MyListActivity::class.java)
        startActivity(intent)
    }
    fun goToSearch(view: View) {
        val intent: Intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}
