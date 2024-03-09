package com.kochkov.evgeny.setlist_mobile.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.kochkov.evgeny.setlist_mobile.R
import java.text.SimpleDateFormat
import java.util.*

class ConcertDateView@JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null): View(context, attributeSet) {

    private val defaultDay = "01"
    private val defaultMonth = "NOV"
    private val defaultYear = "2021"

    private lateinit var backgroundPaint: Paint
    private lateinit var textDayPaint: Paint
    private lateinit var textMonthPaint: Paint
    private lateinit var textYearPaint: Paint

    private var sDay = defaultDay
    private var sMonth = defaultMonth
    private var sYear = defaultYear


    private val backgroundRect = RectF()

    init {
        Log.d("BMTH", "init")
        initPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d("BMTH", "onMeasure")
        //Считаем полный размер с паддингами
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec) + paddingLeft + paddingRight

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec) + paddingBottom + paddingTop

        //val chosenWidth = chooseDimension(widthMode, widthSize)
        //val chosenHeight = chooseDimension(heightMode, heightSize)

        val resolvedWidth = resolveSize(widthSize, widthMeasureSpec)
        val resolvedHeight = resolveSize(heightSize, heightMeasureSpec)

        val minSide = Math.min(resolvedWidth, resolvedHeight)


        setMeasuredDimension(minSide, minSide)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d("BMTH", "onSizeChanged")
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun chooseDimension(mode: Int, size: Int) =
            when (mode) {
                MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
                else -> 300
            }

    private fun initPaint() {
        backgroundPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.colorBlue)
            style = Paint.Style.FILL_AND_STROKE
        }

        textDayPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.colorWhite)
            style = Paint.Style.FILL_AND_STROKE
            textSize = 60F
        }

        textYearPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.colorWhite)
            style = Paint.Style.FILL_AND_STROKE
            textSize = 60F
        }

        textMonthPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.colorWhite)
            style = Paint.Style.FILL_AND_STROKE
            textSize = 60F
        }
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("BMTH", "onDraw")
        drawBackground(canvas)
        drawDayText(canvas)
        drawMonthText(canvas)
        drawYearText(canvas)
    }

    private fun drawDayText(canvas: Canvas) {
        val message = sDay
        val widths = FloatArray(message.length)

        textDayPaint.textSize = getDayTextSize()
        textDayPaint.getTextWidths(message, widths)

        var advance = 0f
        widths.forEach {
            advance += it
        }

        val x = width / 2 - advance / 2
        val y = height / 2 + advance / 4
        //Рисуем наш текст
        canvas.drawText(message, x, y, textDayPaint)
    }

    private fun drawMonthText(canvas: Canvas) {
        val message = sMonth
        val widths = FloatArray(message.length)
        textMonthPaint.textSize = getMonthTextSize()
        textMonthPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        //Рисуем наш текст
        canvas.drawText(message, width / 2 - advance / 2, height / 8.8f + advance / 4, textMonthPaint)
    }

    private fun drawYearText(canvas: Canvas) {
        val message = sYear
        val widths = FloatArray(message.length)
        textYearPaint.textSize = getYearTextSize()
        textYearPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        //Рисуем наш текст
        canvas.drawText(message, width / 2 - advance / 2, height / 1.2f + advance / 4, textYearPaint)
    }

    private fun drawBackground(canvas: Canvas) {
        backgroundRect.set(0f, 0f, height.toFloat(), width.toFloat())
        val cornerSize = getCornerRoundSize()
        canvas.drawRoundRect(backgroundRect, cornerSize, cornerSize, backgroundPaint)
    }

    private fun getDayTextSize(): Float {
        return height / 2.2f
    }

    private fun getMonthTextSize(): Float {
        return height / 5f
    }

    private fun getYearTextSize(): Float {
        return getMonthTextSize()
    }

    private fun getCornerRoundSize() : Float {
        return height / 20f
    }

    fun setDate(date: Date) {
        val month = SimpleDateFormat("mm", Locale.getDefault())
        sMonth = identMonth(month.format(date))
        val day = SimpleDateFormat("dd", Locale.getDefault())
        sDay = day.format(date)
        val year = SimpleDateFormat("yyyy", Locale.getDefault())
        sYear = year.format(date)
        initPaint()
        invalidate()
    }

    private fun identMonth(format: String): String {
        return when (format) {
            "01" -> context.resources.getString(R.string.jan)
            "02" -> context.resources.getString(R.string.feb)
            "03" -> context.resources.getString(R.string.mar)
            "04" -> context.resources.getString(R.string.apr)
            "05" -> context.resources.getString(R.string.may)
            "06" -> context.resources.getString(R.string.jun)
            "07" -> context.resources.getString(R.string.jul)
            "08" -> context.resources.getString(R.string.aug)
            "09" -> context.resources.getString(R.string.sep)
            "10" -> context.resources.getString(R.string.oct)
            "11" -> context.resources.getString(R.string.nov)
            "12" -> context.resources.getString(R.string.nov)
            else -> context.resources.getString(R.string.jan)
        }
    }

    fun setDate(day: String, month: String, year: String) {
        sDay = day
        sMonth = month
        sYear = year
        initPaint()
        invalidate()
    }
}