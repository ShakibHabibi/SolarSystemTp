package com.shkbhbb.solarsystemtp

import android.content.Context
import android.graphics.Paint
import android.text.TextPaint
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.sin

class Planet(val name: String) {

    companion object {
        private val planetColors = listOf(
            R.color.green_00 to R.color.green_00_1a,
            R.color.purple_ba to R.color.purple_ba_1a,
            R.color.red_f1 to R.color.red_f1_1a,
            R.color.blue_52 to R.color.blue_52_1a,
            R.color.purple_8a to R.color.purple_8a_1a,
            R.color.purple_8a to R.color.purple_8a_1a,
            R.color.purple_8a to R.color.purple_8a_1a,
            R.color.purple_8a to R.color.purple_8a_1a
        )
        private val layers = listOf(1, 2, 3, 4, 5, 5, 5, 5)
        private val degrees = listOf(315.0, 45.0, 135.0, 225.0)

        private fun getPlanetColor(index: Int): Int = planetColors[index].first

        private fun getPlanetBackgroundColor(index: Int): Int = planetColors[index].second

        fun getPlanetTextPaint(solarSystemTP: SolarSystemTP, index: Int): TextPaint =
            TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = ContextCompat.getColor(solarSystemTP.context, getPlanetColor(index))
                textSize = solarSystemTP.planetTextSize
            }

        fun getPlanetBackgroundPaint(context: Context, index: Int): Paint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = ContextCompat.getColor(context, getPlanetBackgroundColor(index))
                style = Paint.Style.FILL
            }

        fun getCurrentPlanetPaint(context: Context, index: Int): Paint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = ContextCompat.getColor(context, getPlanetColor(index))
            }

        private fun getMargin(index: Int, solarSystemTP: SolarSystemTP): Float =
            layers[index] * solarSystemTP.ringsMargin + solarSystemTP.finalProgressWidth

        fun getXY(index: Int, solarSystemTP: SolarSystemTP): Pair<Float, Float> {
            val centerX = solarSystemTP.measuredWidth / 2f
            val centerY = solarSystemTP.measuredHeight / 2f

            return when (index) {
                0 -> Pair(centerX, centerY - getMargin(index, solarSystemTP))
                1 -> Pair(centerX + getMargin(index, solarSystemTP), centerY)
                2 -> Pair(centerX, centerY + getMargin(index, solarSystemTP))
                3 -> Pair(centerX - getMargin(index, solarSystemTP), centerY)
                in 4..7 -> {
                    val radius = getMargin(index, solarSystemTP)
                    val sinX = centerX + (cos(Math.toRadians(degrees[index - 4])) * radius)
                    val cosY = centerY + (sin(Math.toRadians(degrees[index - 4])) * radius)
                    Pair(sinX.toFloat(), cosY.toFloat())
                }
                else -> Pair(0f, 0f)
            }
        }
    }
}