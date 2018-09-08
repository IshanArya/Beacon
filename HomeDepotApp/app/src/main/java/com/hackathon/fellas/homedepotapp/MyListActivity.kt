package com.hackathon.fellas.homedepotapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class MyListActivity : AppCompatActivity() {

    lateinit var myList: ListView

    /**
     * Taken from SharedData.tools (if tools is changed, this will break!)
     */
    val toolResults: MutableList<String> = mutableListOf(
            "Multi-bit Screwdriver",
            "Steel Drilling Hammer",
            "Sinker Nails",
            "Fiberglass Claw Hammer"
    )
    lateinit var toolAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list)

        myList = findViewById(R.id.my_list)
        toolAdapter = ArrayAdapter(this, R.layout.tool_item, R.id.tool_name, toolResults)

        myList.adapter = toolAdapter

        myList.setOnItemClickListener{parent, view, position, id ->
            var itemKey: String = myList.getItemAtPosition(position) as String
//            Toast.makeText(this, SharedData.tools[itemKey]?.item, Toast.LENGTH_SHORT).show()
            val intent: Intent = Intent(this, NavigationActivity::class.java).apply {
                putExtra("Node", SharedData.tools[itemKey])
            }
            startActivity(intent)
        }

    }

}
