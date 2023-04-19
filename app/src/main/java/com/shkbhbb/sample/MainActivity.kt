package com.shkbhbb.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shkbhbb.solarsystemtp.Planet

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var index = 0

        val planets: List<Planet> = listOf(
            Planet("Galaxy A52"),
            Planet("My PC"),
            Planet("Galaxy A52"),
            Planet("iPhone 8"),
            Planet("Galaxy Tab34"),
            Planet("Galaxy A52"),
            Planet("iPhone 8"),
            Planet("iPhone 8"),
            Planet("My PC")
        )
    }
}