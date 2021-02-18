/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package com.example.android.dessertpusher
//
//import android.content.ActivityNotFoundException
//import android.os.Bundle
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ShareCompat
//import androidx.databinding.DataBindingUtil
//import androidx.lifecycle.LifecycleObserver
//import com.example.android.dessertpusher.databinding.ActivityMainBinding
//import timber.log.Timber
//
///** onSaveInstanceState Bundle Keys **/
//const val KEY_REVENUE = "revenue_key"
//const val KEY_DESSERT_SOLD = "dessert_sold_key"
//const val KEY_TIMER_SECONDS = "timer_seconds_key"
//
//// note that Timber also needs to be setup in a special Application-level overwrite of
//// onCreate in the PusherApplication.kt, as well as imported in the build.gradule script
//// in the "module" script, not "project" via "implementation 'com.jakewharton.timber:timber:4.7.1'"
//
//// note that you can kill an app in the emulator that is in background with code below
//// go to terminal and enter: "adb shell am kill com.example.android.dessertpusher" or <app name>
//
//// note that hitting "control + o" brings up searchable list of functions
//
//class MainActivity : AppCompatActivity(), LifecycleObserver {
//
//    private var revenue = 0
//    private var dessertsSold = 0
//
//    // make a instance of DessertTimer class from DessertTimer.kt file
//    private lateinit var dessertTimer: DessertTimer
//
//    // Contains all the views
//    private lateinit var binding: ActivityMainBinding
//
//    /** Dessert Data **/
//
//    /**
//     * Simple data class that represents a dessert. Includes the resource id integer associated with
//     * the image, the price it's sold for, and the startProductionAmount, which determines when
//     * the dessert starts to be produced.
//     */
//    data class Dessert(val imageId: Int, val price: Int, val startProductionAmount: Int)
//
//    // Create a list of all desserts, in order of when they start being produced
//    private val allDesserts = listOf(
//            Dessert(R.drawable.cupcake, 5, 0),
//            Dessert(R.drawable.donut, 10, 5),
//            Dessert(R.drawable.eclair, 15, 20),
//            Dessert(R.drawable.froyo, 30, 50),
//            Dessert(R.drawable.gingerbread, 50, 100),
//            Dessert(R.drawable.honeycomb, 100, 200),
//            Dessert(R.drawable.icecreamsandwich, 500, 500),
//            Dessert(R.drawable.jellybean, 1000, 1000),
//            Dessert(R.drawable.kitkat, 2000, 2000),
//            Dessert(R.drawable.lollipop, 3000, 4000),
//            Dessert(R.drawable.marshmallow, 4000, 8000),
//            Dessert(R.drawable.nougat, 5000, 16000),
//            Dessert(R.drawable.oreo, 6000, 20000)
//    )
//    private var currentDessert = allDesserts[0]
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // print log statement when onCreate called
//        // note the "i" in Log.i is the log level
//        // options are initials for: verbose, debug, info, warn, error, assert
//        Log.i("MainActivity", "onCreate called")
//
//        // Use Data Binding to get reference to the views
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//
//        binding.dessertButton.setOnClickListener {
//            onDessertClicked()
//        }
//
//        // create dessertTimer
//        // the code below was used at first for generic timer,
//        // before upgrading to use LifecycleObserver
//        // dessertTimer = DessertTimer()
//
//        // create dessertTimer using LifecycleObserver
//        // note that "this" refers to MainActivity
//        dessertTimer = DessertTimer(this.lifecycle)
//
//        // check to see if savedInstanceState == NULL
//        // it is NULL by default, but will become not NULL if the app has gone into background
//        // and the current call to onCreate is actually recreating the app
//        // this will allow for re-using app data from saved instance key/value pairs
//        // in this case, will fetch and re-use the revenue value, and set it as the revenue variable
////        if (savedInstanceState != null) {
////            Timber.i("savedInstanceState is not equal to null, re-using data")
////            revenue = savedInstanceState.getInt(KEY_REVENUE)
////            dessertsSold = savedInstanceState.getInt("key_desserts_sold")
////        }
//        if (savedInstanceState != null) {
//            // Get all the game state information from the bundle, set it
//            revenue = savedInstanceState.getInt(KEY_REVENUE, 0)
//            dessertsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0)
//            dessertTimer.secondsCount = savedInstanceState.getInt(KEY_TIMER_SECONDS, 0)
//        }
//
//
//        // Set the TextViews to the right values
//        binding.revenue = revenue
//        binding.amountSold = dessertsSold
//
//        // Make sure the correct dessert is showing
//        binding.dessertButton.setImageResource(currentDessert.imageId)
//    }
//
//    /**
//     * Updates the score when the dessert is clicked. Possibly shows a new dessert.
//     */
//    private fun onDessertClicked() {
//
//        // Update the score
//        revenue += currentDessert.price
//        dessertsSold++
//
//        binding.revenue = revenue
//        binding.amountSold = dessertsSold
//
//        // Show the next dessert
//        showCurrentDessert()
//    }
//
//    /**
//     * Determine which dessert to show.
//     */
//    private fun showCurrentDessert() {
//        var newDessert = allDesserts[0]
//        for (dessert in allDesserts) {
//            if (dessertsSold >= dessert.startProductionAmount) {
//                newDessert = dessert
//            }
//            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
//            // you'll start producing more expensive desserts as determined by startProductionAmount
//            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
//            // than the amount sold.
//            else break
//        }
//
//        // If the new dessert is actually different than the current dessert, update the image
//        if (newDessert != currentDessert) {
//            currentDessert = newDessert
//            binding.dessertButton.setImageResource(newDessert.imageId)
//        }
//    }
//
//    // override onSaveInstanceState
//    // lets you save any current data from app at time it's put into background
//    // this allows your app to avoid losing state info when android OS puts it into background
//    // the bundle object lets you add key/value pairs, but note that the bundle is stored in RAM
//    // so it's advised to keep the data saved this way minimal to avoid overhead (< 100 kb)
////    override fun onSaveInstanceState(outState: Bundle) {
////        super.onSaveInstanceState(outState)
////        Timber.i("onSaveInstanceState called")
////
////        // add key/value pairs of data to save
////        // note the key name for the key/value pair can be a simple string, like below
//////        outState.putInt("key_revenue", revenue)
////        // but it's advised to use a constant in the key/value pair, with the constant
////        // defined at the top (not really sure if this is just OCD stuff?)
////        outState.putInt(KEY_REVENUE, revenue)
////        outState.putInt("key_desserts_sold", dessertsSold)
////    }
//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putInt(KEY_REVENUE, revenue)
//        outState.putInt(KEY_DESSERT_SOLD, dessertsSold)
//        outState.putInt(KEY_TIMER_SECONDS, dessertTimer.secondsCount)
//        Timber.i("onSaveInstanceState Called")
//        super.onSaveInstanceState(outState)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        Timber.i("onRestoreInstanceState Called")
//    }
//
//
//    /**
//     * Menu methods
//     */
//    private fun onShare() {
//
//        // log onShare() called
//        Timber.i("onShare called")
//
//        val shareIntent = ShareCompat.IntentBuilder.from(this)
//                .setText(getString(R.string.share_text, dessertsSold, revenue))
//                .setType("text/plain")
//                .intent
//        try {
//            startActivity(shareIntent)
//        } catch (ex: ActivityNotFoundException) {
//            Toast.makeText(this, getString(R.string.sharing_not_available),
//                    Toast.LENGTH_LONG).show()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.shareMenuButton -> onShare()
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    // create onStart()
//    override fun onStart() {
//        super.onStart()
//
//        // create log for onStart()
//        // Log.i("MainActivity", "onStart called")
//
//        // use timber log statement instead of default log statement (see above)
//        Timber.i("onStart called")
//
//        // set dessertsSold to 0
//        dessertsSold = 0
//
//        // original code to start dessertTimer,
//        // but replaced with lifecycle observer that annotates start method in DessertTimer.kt
//        // and specifies for it to be called on start
//        // dessertTimer.startTimer()
//    }
//
//    // create onRestart()
//    override fun onRestart() {
//        super.onRestart()
//
//        // create log for onRestart()
//        Timber.i("onRetart called")
//    }
//
//    // create onResume()
//    override fun onResume() {
//        super.onResume()
//
//        // create log for onResume()
//        Timber.i("onResume called")
//    }
//
//    // create onPause()
//    override fun onPause() {
//        super.onPause()
//
//        // create log for onPause()
//        Timber.i("onPause called")
//    }
//
//    // create onStop()
//    override fun onStop() {
//        super.onStop()
//
//        // create log for onStop()
//        Timber.i("onStop called")
//
//        // stop dessertTimer
//        // original code to stop dessertTimer,
//        // but replaced with lifecycle observer that annotates stop method in DessertTimer.kt
//        // and specifies for it to be called on stop
//        // dessertTimer.stopTimer()
//    }
//
//    // create onDestroy()
//    override fun onDestroy() {
//        super.onDestroy()
//
//        // create log for onDestroy()
//        Timber.i("onDestroy called")
//    }
//}










package com.example.android.dessertpusher

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import com.example.android.dessertpusher.databinding.ActivityMainBinding
import timber.log.Timber

/** onSaveInstanceState Bundle Keys **/
const val KEY_REVENUE = "revenue_key"
const val KEY_DESSERT_SOLD = "dessert_sold_key"
const val KEY_TIMER_SECONDS = "timer_seconds_key"

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private var revenue = 0
    private var dessertsSold = 0
    private lateinit var dessertTimer: DessertTimer

    // Contains all the views
    private lateinit var binding: ActivityMainBinding

    /** Dessert Data **/

    /**
     * Simple data class that represents a dessert. Includes the resource id integer associated with
     * the image, the price it's sold for, and the startProductionAmount, which determines when
     * the dessert starts to be produced.
     */
    data class Dessert(val imageId: Int, val price: Int, val startProductionAmount: Int)

    // Create a list of all desserts, in order of when they start being produced
    private val allDesserts = listOf(
        Dessert(R.drawable.cupcake, 5, 0),
        Dessert(R.drawable.donut, 10, 5),
        Dessert(R.drawable.eclair, 15, 20),
        Dessert(R.drawable.froyo, 30, 50),
        Dessert(R.drawable.gingerbread, 50, 100),
        Dessert(R.drawable.honeycomb, 100, 200),
        Dessert(R.drawable.icecreamsandwich, 500, 500),
        Dessert(R.drawable.jellybean, 1000, 1000),
        Dessert(R.drawable.kitkat, 2000, 2000),
        Dessert(R.drawable.lollipop, 3000, 4000),
        Dessert(R.drawable.marshmallow, 4000, 8000),
        Dessert(R.drawable.nougat, 5000, 16000),
        Dessert(R.drawable.oreo, 6000, 20000)
    )
    private var currentDessert = allDesserts[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate Called")

        // Use Data Binding to get reference to the views
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.dessertButton.setOnClickListener {
            onDessertClicked()
        }

        // Setup dessertTimer, passing in the lifecycle
        dessertTimer = DessertTimer(this.lifecycle)

        // If there is a savedInstanceState bundle, then you're "restarting" the activity
        // If there isn't a bundle, then it's a "fresh" start
        if (savedInstanceState != null) {
            Timber.i("savedInstanceState is not null")
            // Get all the game state information from the bundle, set it
            revenue = savedInstanceState.getInt(KEY_REVENUE, 0)
            dessertsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0)
            dessertTimer.secondsCount = savedInstanceState.getInt(KEY_TIMER_SECONDS, 0)
            showCurrentDessert()

        }

        // Set the TextViews to the right values
        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Make sure the correct dessert is showing
        binding.dessertButton.setImageResource(currentDessert.imageId)
    }

    /**
     * Updates the score when the dessert is clicked. Possibly shows a new dessert.
     */
    private fun onDessertClicked() {

        // Update the score
        revenue += currentDessert.price
        dessertsSold++

        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Show the next dessert
        showCurrentDessert()
    }

    /**
     * Determine which dessert to show.
     */
    private fun showCurrentDessert() {
        var newDessert = allDesserts[0]
        for (dessert in allDesserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                newDessert = dessert
            }
            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
            // you'll start producing more expensive desserts as determined by startProductionAmount
            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
            // than the amount sold.
            else break
        }

        // If the new dessert is actually different than the current dessert, update the image
        if (newDessert != currentDessert) {
            currentDessert = newDessert
            binding.dessertButton.setImageResource(newDessert.imageId)
        }
    }

    /**
     * Menu methods
     */
    private fun onShare() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setText(getString(R.string.share_text, dessertsSold, revenue))
            .setType("text/plain")
            .intent
        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.sharing_not_available),
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareMenuButton -> onShare()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Called when the user navigates away from the app but might come back
     */
    // it's weird, even when commenting out the saveInstanceState storage of key/value pairs
    // the app still retains a knowledge of the values upon restart
    // i presume this means that the app isn't being fully killed by android os
    // since then i would see the onRestoreInstanceState log message
    // and that is the only time the values would need to be re-used from the saved instance
    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putInt(KEY_REVENUE, revenue)
//        outState.putInt(KEY_DESSERT_SOLD, dessertsSold)
//        outState.putInt(KEY_TIMER_SECONDS, dessertTimer.secondsCount)
        Timber.i("onSaveInstanceState Called test")
//        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.i("onRestoreInstanceState Called")
    }

    /** Lifecycle Methods **/
    override fun onStart() {
        super.onStart()
        Timber.i("onStart Called test")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart Called")
    }
}