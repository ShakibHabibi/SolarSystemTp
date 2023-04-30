package com.shkbhbb.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shkbhbb.solarsystemtp.Planet
import com.shkbhbb.solarsystemtp.SolarSystemTP

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val planets: List<Planet> = listOf(
            Planet("Galaxy A52"),
            Planet("My PC"),
            Planet("Galaxy A52"),
            Planet("iPhone 8"),
            Planet("Galaxy Tab34"),
            Planet("Galaxy A52")
        )

        val solarSystemTP = findViewById<SolarSystemTP>(R.id.solar)
        solarSystemTP.setPlanets(planets)
        solarSystemTP.setOnPlanetListener(object : SolarSystemTP.PlanetListener {
            override fun onPlanetSelected(index: Int, planet: Planet) {
                Toast.makeText(this@MainActivity, "$index ${planet.name}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}