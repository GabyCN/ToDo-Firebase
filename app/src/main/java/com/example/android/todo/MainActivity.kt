package com.example.android.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread(Runnable {
            var url = URL("https://parseapi.back4app.com/ToDoList")
            var urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("X-Parse-Application-Id", "UTBkCSzxwz749f6H0M6LeHXRxLTZoYo44LDmsVHA") // This is the fake app's application id
            urlConnection.setRequestProperty("X-Parse-Master-Key", "Ca0NNR3vgrFwAdOJsPffmLWyPw0KHcf3K14iNi17") // This is the fake app's readonly master key
            try {
                val data = JSONObject(urlConnection.inputStream.bufferedReader().use { it.readText() }) // Here you have the data that you need
                Log.d("MainActivity", data.toString(2))
            } catch (e: Exception) {
                Log.e("MainActivity", e.toString())
            } finally {
                urlConnection.disconnect()
            }
        }).start()


        // Initializing the array lists and the adapter
        var itemlist = arrayListOf<String>()
        var adapter =ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice
                , itemlist)
        // Adding the items to the list when the add button is pressed
        add.setOnClickListener {

            itemlist.add(editText.text.toString())
            listView.adapter =  adapter
            adapter.notifyDataSetChanged()
            // This is because every time when you add the item the input space or the eidt text space will be cleared
            editText.text.clear()
        }
        // Clearing all the items in the list when the clear button is pressed
        clear.setOnClickListener {

            itemlist.clear()
            adapter.notifyDataSetChanged()
        }
        // Adding the toast message to the list when an item on the list is pressed
        listView.setOnItemClickListener { adapterView, view, i, l ->
            android.widget.Toast.makeText(this, "You Selected the item --> "+itemlist.get(i), android.widget.Toast.LENGTH_SHORT).show()
        }
        // Selecting and Deleting the items from the list when the delete button is pressed
        delete.setOnClickListener {
            val position: SparseBooleanArray = listView.checkedItemPositions
            val count = listView.count
            var item = count - 1
            while (item >= 0) {
                if (position.get(item))
                {
                    adapter.remove(itemlist.get(item))
                }
                item--
            }
            position.clear()
            adapter.notifyDataSetChanged()
        }
    }
}