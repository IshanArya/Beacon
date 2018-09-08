package com.hackathon.fellas.homedepotapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView


class NavigationActivity : AppCompatActivity() {

    private var arrowImageView: ImageView? = null
    private var direction: Direction? = null

    enum class Direction {
        RIGHT, LEFT, FORWARD
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        arrowImageView = findViewById(R.id.arrowImageView)
        arrowImageView?.setImageResource(R.drawable.arrow_forward)
    }

    fun changeDirection(direction: Direction) {
        this.direction = direction
    }

    fun onButtonPressed(view: View) {
        var button = view as? Button
        val text = button?.getText().toString().toLowerCase()
        if(text == "forward") {
            arrowImageView?.setImageResource(R.drawable.arrow_forward)
        }
        else if(text == "left") {
            arrowImageView?.setImageResource(R.drawable.arrow_left)
        }
        else if(text == "right") {
            arrowImageView?.setImageResource(R.drawable.arrow_right)
        }
        Log.i("button text", button?.getText().toString())
    }

}
