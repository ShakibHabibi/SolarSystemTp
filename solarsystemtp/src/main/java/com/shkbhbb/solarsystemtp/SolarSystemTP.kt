package com.shkbhbb.solarsystemtp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.RectF
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlin.math.min


class SolarSystemTP(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var finalProgressWidth: Float
    private val ringColors: List<Int> = listOf(
        R.color.black_32, R.color.black_24, R.color.black_16, R.color.black_8, R.color.black_5
    )

    private var planetListener: PlanetListener? = null

    private var planets: MutableList<Planet> = mutableListOf()
    private var planetRecs: MutableList<RectF> = mutableListOf()

    private var progress: Float
    private val progressWidth: Float
    private var progressColor: Int
    private val progressBgColor: Int
    private val progressRadius: Float
    private var sweepAngle: Float = 0F
    private val numberOfRings: Int
    private val ringsWidth: Float
    private var centerText: String
    private val centerTextSize: Float
    private var centerTextColor: Int
    private var centerTextFont: Int
    private var centerHelperText: String
    private var centerStyledText: String
    private val centerStyledTextSize: Float
    private var centerStyledTextColor: Int
    private var centerStyledTextFont: Int
    private var planetRadius: Float
    var planetTextSize: Float
    var planetTextFont: Int
    val ringsMargin: Float

    private lateinit var progressPaint: Paint
    private lateinit var progressBgPaint: Paint
    private lateinit var centerTextPaint: TextPaint
    private lateinit var transparentPaint: Paint
    private lateinit var centerStyledTextPaint: TextPaint

    private lateinit var ringRec: RectF
    private lateinit var progressRec: RectF

    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.SolarSystemTp, 0, 0
        ).apply {
            try {
                progress = getFloat(R.styleable.SolarSystemTp_progress, 25f)
                progressWidth = getDimension(R.styleable.SolarSystemTp_progress_width, 16.dpToPx())
                progressRadius =
                    getDimension(R.styleable.SolarSystemTp_progress_radius, 68.dpToPx())
                progressColor =
                    getResourceId(R.styleable.SolarSystemTp_progress_color, R.color.orange_ef_33)
                progressBgColor =
                    getResourceId(R.styleable.SolarSystemTp_progress_bg_color, R.color.orange_ef)

                numberOfRings = getInteger(R.styleable.SolarSystemTp_number_of_rings, 5)
                ringsMargin = getDimension(R.styleable.SolarSystemTp_rings_margin, 16.dpToPx())
                ringsWidth = getDimension(R.styleable.SolarSystemTp_rings_width, 1.dpToPx())

                centerText = getString(R.styleable.SolarSystemTp_center_text) ?: ""
                centerTextSize =
                    getDimension(R.styleable.SolarSystemTp_center_text_size, 14.spToPx())
                centerTextColor =
                    getResourceId(R.styleable.SolarSystemTp_center_text_color, R.color.black_87)
                centerTextFont =
                    getResourceId(R.styleable.SolarSystemTp_center_text_font, R.font.pjs_regular)
                centerHelperText = getString(R.styleable.SolarSystemTp_center_helper_text) ?: ""
                centerStyledText = getString(R.styleable.SolarSystemTp_center_styled_text) ?: ""
                centerStyledTextSize =
                    getDimension(R.styleable.SolarSystemTp_center_styled_text_size, 18.spToPx())
                centerStyledTextColor = getResourceId(
                    R.styleable.SolarSystemTp_center_styled_text_color, R.color.black_87
                )
                centerStyledTextFont = getResourceId(
                    R.styleable.SolarSystemTp_center_styled_text_font, R.font.pjs_bold
                )

                planetRadius = getFloat(R.styleable.SolarSystemTp_planet_radius, 8.dpToPx())
                planetTextSize =
                    getDimension(R.styleable.SolarSystemTp_planet_text_size, 10.spToPx())
                planetTextFont =
                    getResourceId(R.styleable.SolarSystemTp_planet_text_font, R.font.pjs_regular)

                finalProgressWidth = (progressRadius + progressWidth - 8.dpToPx())

                setUpPaint()
                calculateAngle()
            } finally {
                recycle()
            }
        }
    }

    private fun setUpRec() {
        progressRec = RectF(
            (measuredWidth / 2) - progressRadius,
            (measuredHeight / 2) - progressRadius,
            (measuredWidth / 2) + progressRadius,
            (measuredHeight / 2) + progressRadius
        )
        ringRec = RectF(
            (measuredWidth / 2) - progressRadius - ringsMargin,
            (measuredHeight / 2) - progressRadius - ringsMargin,
            (measuredWidth / 2) + progressRadius + ringsMargin,
            (measuredHeight / 2) + progressRadius + ringsMargin
        )
    }

    private fun setUpPaint() {
        transparentPaint = Paint(ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.transparent)
        }

        progressPaint = Paint(ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, progressColor)
            strokeWidth = progressWidth
            strokeCap = Paint.Cap.SQUARE
            style = Paint.Style.STROKE
        }

        progressBgPaint = Paint(ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, progressBgColor)
            strokeWidth = progressWidth
            style = Paint.Style.STROKE
        }

        centerTextPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, centerTextColor)
            textSize = centerTextSize
            typeface = ResourcesCompat.getFont(context, centerTextFont)
        }

        centerStyledTextPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, centerStyledTextColor)
            textSize = centerStyledTextSize
            typeface = ResourcesCompat.getFont(context, centerStyledTextFont)
        }
    }


    private fun getRingColor(index: Int): Int = ContextCompat.getColor(context, ringColors[index])

    private fun getRingPaint(index: Int): Paint {
        return Paint(ANTI_ALIAS_FLAG).apply {
            color = getRingColor(index - 1)
            strokeWidth = ringsWidth
            strokeCap = Paint.Cap.SQUARE
            style = Paint.Style.STROKE
        }
    }

    private fun calculateAngle() {
        sweepAngle = (360F * progress) / 100F
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        setUpRec()
        canvas?.let {
            drawProgress(it)
            drawRings(it)
            drawPlanets(it)
            drawCenterText(it)
            drawCenterHelperText(it)
            drawCenterStyledText(it)
        }
    }

    private fun drawProgress(canvas: Canvas) {
        canvas.drawArc(
            progressRec, 270f, sweepAngle - 360, false, progressBgPaint
        )
        canvas.drawArc(progressRec, 270f, sweepAngle, false, progressPaint)
    }

    private fun drawRings(canvas: Canvas) {
        for (i in 1..numberOfRings) {
            canvas.drawArc(
                (measuredWidth / 2) - finalProgressWidth - (ringsMargin * i),
                (measuredHeight / 2) - finalProgressWidth - (ringsMargin * i),
                (measuredWidth / 2) + finalProgressWidth + (ringsMargin * i),
                (measuredHeight / 2) + finalProgressWidth + (ringsMargin * i),
                0f,
                360f,
                false,
                getRingPaint(i)
            )
        }
    }

    private fun drawPlanets(canvas: Canvas) {
        planetRecs.clear()

        for (i in planets.indices) {
            val (x, y) = Planet.getXY(i, this@SolarSystemTP)
            canvas.drawCircle(x, y, planetRadius, Planet.getCurrentPlanetPaint(context, i))

            val nameStaticLayout = drawPlanetText(canvas, i, x, y)
            drawPlanetBackground(canvas, i, nameStaticLayout, x, y)
            drawArea(canvas, nameStaticLayout, x, y)
        }
    }

    private fun drawArea(canvas: Canvas, nameStaticLayout: StaticLayout, x: Float, y: Float) {
        val area = RectF(
            x - (nameStaticLayout.width / 2f) - 4.dpToPx(),
            y - (nameStaticLayout.height) - 13.dpToPx(),
            x - (nameStaticLayout.width / 2f) + nameStaticLayout.width + 4.dpToPx(),
            y + planetRadius
        )
        canvas.drawRect(area, transparentPaint)
        planetRecs.add(area)
    }

    private fun drawPlanetText(canvas: Canvas, index: Int, x: Float, y: Float): StaticLayout {
        val name = planets[index].name
        val ellipsizeMax = "1234567890"

        val planetTextPaint = Planet.getPlanetTextPaint(this, index)

        val ellipsizedText = TextUtils.ellipsize(
            name,
            planetTextPaint,
            planetTextPaint.measureText(ellipsizeMax),
            TextUtils.TruncateAt.END
        )
        val ellipsizedTextWidth = planetTextPaint.measureText(ellipsizedText.toString())

        val nameStaticLayBuilder = StaticLayout.Builder.obtain(
            ellipsizedText, 0, ellipsizedText.length, planetTextPaint, ellipsizedTextWidth.toInt()
        ).setAlignment(Layout.Alignment.ALIGN_CENTER)

        val nameStyledStaticLay = nameStaticLayBuilder.build()

        canvas.save()
        canvas.translate(
            (x - (nameStyledStaticLay.width / 2f)),
            (y - (nameStyledStaticLay.height / 2f) - 20.dpToPx())
        )

        nameStyledStaticLay.draw(canvas)
        canvas.restore()

        return nameStyledStaticLay
    }

    private fun drawPlanetBackground(
        canvas: Canvas, index: Int, nameStaticLayout: StaticLayout, x: Float, y: Float
    ) {
        val backgroundPaint = Planet.getPlanetBackgroundPaint(context, index)

        canvas.drawRoundRect(
            x - (nameStaticLayout.width / 2f) - 4.dpToPx(),
            y - (nameStaticLayout.height) - 13.dpToPx(),
            x - (nameStaticLayout.width / 2f) + nameStaticLayout.width + 4.dpToPx(),
            y - 13.dpToPx(),
            8.dpToPx(),
            8.dpToPx(),
            backgroundPaint
        )
    }

    private fun drawCenterText(canvas: Canvas) {
        if (centerText.isEmpty()) return

        val centerStaticLayBuilder = StaticLayout.Builder.obtain(
            centerText, 0, centerText.length, centerTextPaint, (progressRadius * 2).toInt()
        ).setAlignment(Layout.Alignment.ALIGN_CENTER).setLineSpacing(4.dpToPx(), 1f)
        val centerStaticLay = centerStaticLayBuilder.build()

        canvas.save()
        canvas.translate(
            ((measuredWidth / 2f) - (centerStaticLay.width / 2f)),
            ((measuredHeight / 2f) - (centerStaticLay.height / 2f))
        )

        centerStaticLay.draw(canvas)
        canvas.restore()
    }

    private fun drawCenterHelperText(canvas: Canvas) {
        if (centerStyledText.isEmpty()) return

        val width = centerStyledTextPaint.measureText(centerStyledText)

        val centerStaticLayBuilder = StaticLayout.Builder.obtain(
            centerHelperText,
            0,
            centerHelperText.length,
            centerTextPaint,
            (progressRadius * 2).toInt()
        ).setAlignment(Layout.Alignment.ALIGN_CENTER).setLineSpacing(4.dpToPx(), 1f)
        val centerStaticLay = centerStaticLayBuilder.build()

        canvas.save()
        canvas.translate(
            ((measuredWidth / 2f) - (centerStaticLay.width / 2f)) + width / 2,
            ((measuredHeight / 2f) - (centerStaticLay.height / 2f))
        )

        centerStaticLay.draw(canvas)
        canvas.restore()
    }

    private fun drawCenterStyledText(canvas: Canvas) {
        if (centerStyledText.isEmpty()) return

        val width = centerTextPaint.measureText(centerHelperText)

        val centerStyledStaticLayBuilder = StaticLayout.Builder.obtain(
            centerStyledText,
            0,
            centerStyledText.length,
            centerStyledTextPaint,
            (progressRadius * 2).toInt()
        ).setAlignment(Layout.Alignment.ALIGN_CENTER).setLineSpacing(2.dpToPx(), 1f)
        val centerStyledStaticLay = centerStyledStaticLayBuilder.build()

        canvas.save()
        canvas.translate(
            ((measuredWidth / 2f) - (centerStyledStaticLay.width / 2f) - width / 2),
            ((measuredHeight / 2f) - (centerStyledStaticLay.height / 2f))
        )

        centerStyledStaticLay.draw(canvas)
        canvas.restore()
    }

    fun setPlanets(newPlanets: List<Planet>) {
        if (newPlanets.size > 8) {
            throw ArrayIndexOutOfBoundsException("Expected size of 8 at max but found ${newPlanets.size}")
        }

        planets = newPlanets.toMutableList()
        invalidate()
    }

    fun addPlanet(newPlanet: Planet) {
        if (planets.size == 8) {
            throw ArrayIndexOutOfBoundsException("Expected size of 8 at max but found 9")
        }

        planets.add(newPlanet)
        invalidate()
    }

    fun removePlanet(newPlanet: Planet) {
        planets.remove(newPlanet)
        invalidate()
    }

    fun setCenterHelperText(text: String) {
        centerHelperText = text
        invalidate()
    }

    fun setProgress(newProgress: Float) {
        progress = newProgress
        calculateAngle()
        invalidate()
    }

    fun setOnPlanetListener(planetListener: PlanetListener) {
        this.planetListener = planetListener
    }

    fun setCenterText(text: String) {
        centerText = text
        invalidate()
    }

    fun setCenterStyledText(text: String) {
        centerStyledText = text
        invalidate()
    }

    fun updateData(centralText: String, centralStyledText: String, newProgress: Float) {
        centerText = centralText
        centerStyledText = centralStyledText
        progress = newProgress

        calculateAngle()
        invalidate()
    }

    fun setProgressColor(newProgressColor: Int) {
        progressColor = newProgressColor
        invalidate()
    }

    fun clearText() {
        centerText = ""
        centerStyledText = ""

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWH = ((finalProgressWidth + (ringsMargin * numberOfRings)) * 2)

        var desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        if (suggestedMinimumWidth == 0) {
            desiredWidth = viewWH.toInt() + paddingLeft + paddingRight
        }

        var desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom
        if (suggestedMinimumHeight == 0) {
            desiredHeight = viewWH.toInt() + paddingTop + paddingBottom
        }

        setMeasuredDimension(
            measureDimension(desiredWidth, widthMeasureSpec),
            measureDimension(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (planetListener == null) {
            return true
        }

        event?.let {
            val selectedX = it.x
            val selectedY = it.y

            when (it.action) {
                MotionEvent.ACTION_UP -> planetRecs.forEachIndexed { index, recF ->
                    if (selectedX in recF.left..recF.right && selectedY in recF.top..recF.bottom) {
                        planetListener!!.onPlanetSelected(index, planets[index])
                    }
                }
            }
        }
        return true
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }

    private fun Int?.dpToPx(): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this?.toFloat() ?: 0F, context.resources.displayMetrics
    )

    private fun Int?.spToPx(): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this?.toFloat() ?: 0F, context.resources.displayMetrics
    )

    interface PlanetListener {
        fun onPlanetSelected(index: Int, planet: Planet)
    }
}
