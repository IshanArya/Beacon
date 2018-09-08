package com.hackathon.fellas.homedepotapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView

class SearchActivity : AppCompatActivity(), TextWatcher {

    lateinit var searchResults: ListView
    lateinit var searchBox: EditText

    var tools: Array<String> = arrayOf(
            "Fiberglass Claw Hammer",
            "Steel Drilling Hammer",
            "Mallet Hammer",
            "Diamond Tip Magnet Screwdriver",
            "Multi-bit Screwdriver",
            "Phillips Head Screwdriver",
            "Panel Board Nails",
            "Sinker Nails",
            "Smooth Shank Common Nails",
            "Drywall Screws",
            "Deck Screws"
    )
    var toolResults: MutableList<String> = mutableListOf()
    lateinit var toolAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchBox = findViewById(R.id.search_box)
        searchResults = findViewById(R.id.search_results)
        searchBox.addTextChangedListener(this)
        toolAdapter = ArrayAdapter(this, R.layout.search_result, R.id.tool_name, toolResults)

        searchResults.adapter = toolAdapter
    }

    override fun afterTextChanged(s: Editable?) {

    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
        if(s != null) {
            toolResults.clear()
            for(tool in tools) {
                if(tool.contains(s, true)) {
                    toolResults.add(tool)
                }
            }
        }
        toolAdapter.notifyDataSetChanged()

    }
}