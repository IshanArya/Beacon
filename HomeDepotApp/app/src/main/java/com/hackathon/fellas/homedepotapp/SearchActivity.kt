package com.hackathon.fellas.homedepotapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast

class SearchActivity : AppCompatActivity(), TextWatcher {

    lateinit var searchResults: ListView
    lateinit var searchBox: EditText

    val tools: Map<String, BluetoothNode> = mapOf<String, BluetoothNode>(
            Pair("Fiberglass Claw Hammer", BluetoothNode.NODE1),
            Pair("Steel Drilling Hammer", BluetoothNode.NODE1),
            Pair("Mallet Hammer", BluetoothNode.NODE1),
            Pair("Diamond Tip Magnet Screwdriver", BluetoothNode.NODE2),
            Pair("Multi-bit Screwdriver", BluetoothNode.NODE2),
            Pair("Phillips Head Screwdriver", BluetoothNode.NODE2),
            Pair("Panel Board Nails", BluetoothNode.NODE3),
            Pair("Sinker Nails", BluetoothNode.NODE3),
            Pair("Smooth Shank Common Nails", BluetoothNode.NODE3),
            Pair("Drywall Screws", BluetoothNode.NODE4),
            Pair("Deck Screws", BluetoothNode.NODE4)
    )
    val toolResults: MutableList<String> = mutableListOf()
    lateinit var toolAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchBox = findViewById(R.id.search_box)
        searchResults = findViewById(R.id.search_results)
        searchBox.addTextChangedListener(this)
        toolAdapter = ArrayAdapter(this, R.layout.search_result, R.id.tool_name, toolResults)

        searchResults.adapter = toolAdapter

        searchResults.setOnItemClickListener{parent, view, position, id ->
            var itemKey: String = searchResults.getItemAtPosition(position) as String
//            Toast.makeText(this, tools[itemKey]?.item, Toast.LENGTH_SHORT).show()
            val intent: Intent = Intent(this, NavigationActivity::class.java).apply {
                putExtra("Node", tools[itemKey])
            }
            startActivity(intent)
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(s != null) {
            toolResults.clear()
            for((key, value) in tools) {
                if(key.contains(s, true)) {
                    toolResults.add(key)
                }
            }
        }
        toolAdapter.notifyDataSetChanged()

    }
}