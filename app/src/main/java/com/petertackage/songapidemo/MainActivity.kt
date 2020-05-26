package com.petertackage.songapidemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.petertackage.songapidemo.feature.audio.provideStreamPlayer
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLogging()
        setContentView(R.layout.main_activity)
    }

    override fun onStop() {
        super.onStop()
        // Prevent audio from running in the background.
        provideStreamPlayer().release()
    }

    private fun initLogging() {
        val loggingTree = Timber.DebugTree()
        Timber.uprootAll()
        Timber.plant(loggingTree)
    }
}